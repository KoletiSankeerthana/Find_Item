package com.wheredidikeepit.app.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.wheredidikeepit.app.utils.engine.ItemRecognitionEngine
import com.wheredidikeepit.app.utils.engine.MlKitBaseRecognitionEngine
import com.wheredidikeepit.app.utils.engine.MlKitObjectDetectorEngine
import com.wheredidikeepit.app.utils.engine.RecognitionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.resume

object ImageMLUtils {

    private val baseEngine: ItemRecognitionEngine = MlKitBaseRecognitionEngine()
    private val objectEngine: ItemRecognitionEngine = MlKitObjectDetectorEngine()

    private var cachedLabels: List<String>? = null

    private fun loadLabels(context: Context): List<String> {
        cachedLabels?.let { return it }
        return try {
            context.assets.open("models/object_labels.txt").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).useLines { lines ->
                    val list = lines.map { it.trim() }.toList()
                    cachedLabels = list
                    Log.d("ML_DEBUG", "Loaded ${list.size} labels from assets/models/object_labels.txt")
                    list
                }
            }
        } catch (e: Exception) {
            Log.e("ML_DEBUG", "Failed to load labels: ${e.message}")
            emptyList()
        }
    }

    suspend fun analyzeImage(context: Context, photoUri: Uri): List<String> = withContext(Dispatchers.IO) {
        Log.d("ML_DEBUG", "=== STARTING IMAGE RECOGNITION FOR URI: $photoUri ===")
        val labelsList = loadLabels(context)

        // Run engines concurrently
        val customJob = async { runCustomLocalModel(context, photoUri, labelsList) }
        val objectJob = async { objectEngine.recognize(context, photoUri) }
        val baseJob = async { baseEngine.recognize(context, photoUri) }

        val customResults = customJob.await()
        val objectResults = objectJob.await()
        val baseResults = baseJob.await()

        Log.d("ML_DEBUG", "RESULTS COUNT -> CustomLocalModel: ${customResults.size}, ObjectDetector: ${objectResults.size}, BaseLabeler: ${baseResults.size}")

        val allCandidateResults = mutableListOf<RecognitionResult>()
        allCandidateResults.addAll(customResults)
        allCandidateResults.addAll(objectResults)
        allCandidateResults.addAll(baseResults)

        if (allCandidateResults.isEmpty()) {
            Log.d("ML_DEBUG", "NO CANDIDATES PRODUCED BY ANY ENGINE")
            return@withContext emptyList()
        }

        // Rank, normalize, and filter results
        val finalSuggestions = allCandidateResults
            .filter { it.confidence >= 0.20f }
            .sortedByDescending { it.confidence }
            .map { result ->
                val normalized = normalizeConsumerLabel(result.label, labelsList)
                Log.d("ML_DEBUG", "RANKING ITEM -> Source: ${result.sourceEngine} | Raw: ${result.label} | Normalized: $normalized | Confidence: ${result.confidence}")
                normalized
            }
            .filter { it.isNotBlank() && it != "Unknown" }
            .distinctBy { it.lowercase() }
            .take(5)

        Log.d("ML_DEBUG", "FINAL SUGGESTIONS TO UI: $finalSuggestions")
        finalSuggestions
    }

    private suspend fun runCustomLocalModel(
        context: Context,
        photoUri: Uri,
        labelsList: List<String>
    ): List<RecognitionResult> = suspendCancellableCoroutine { continuation ->
        try {
            val localModel = LocalModel.Builder()
                .setAssetFilePath("models/object_labeler.tflite")
                .build()

            val options = CustomImageLabelerOptions.Builder(localModel)
                .setConfidenceThreshold(0.10f)
                .build()

            val labeler = ImageLabeling.getClient(options)
            val inputImage = InputImage.fromFilePath(context, photoUri)

            labeler.process(inputImage)
                .addOnSuccessListener { labels ->
                    val results = labels.map { label ->
                        Log.d("ML_DEBUG", "ENGINE: CustomLocalModel | RAW_TEXT: ${label.text} | CONFIDENCE: ${label.confidence} | INDEX: ${label.index}")
                        RecognitionResult(
                            label = label.text,
                            confidence = label.confidence,
                            sourceEngine = "Custom Local TFLite"
                        )
                    }
                    labeler.close()
                    if (continuation.isActive) {
                        continuation.resume(results)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ML_DEBUG", "ENGINE: CustomLocalModel FAILED: ${e.message}")
                    labeler.close()
                    if (continuation.isActive) {
                        continuation.resume(emptyList())
                    }
                }
        } catch (e: Exception) {
            Log.e("ML_DEBUG", "ENGINE: CustomLocalModel EXCEPTION: ${e.message}")
            if (continuation.isActive) {
                continuation.resume(emptyList())
            }
        }
    }

    private fun normalizeConsumerLabel(rawText: String, labelsList: List<String>): String {
        val cleanText = rawText.trim().lowercase()

        val index = cleanText.toIntOrNull()
        val textToMap = if (index != null && index >= 0 && index < labelsList.size) {
            labelsList[index].lowercase()
        } else {
            cleanText
        }

        return when {
            textToMap.contains("water_bottle") || textToMap.contains("water bottle") || textToMap.contains("pop_bottle") || textToMap.contains("water_jug") -> "Water Bottle"
            textToMap.contains("coffee_mug") || textToMap.contains("mug") || textToMap.contains("cup") || textToMap.contains("espresso") -> "Mug"
            textToMap.contains("notebook") || textToMap.contains("laptop") -> "Laptop"
            textToMap.contains("keyboard") || textToMap.contains("keypad") -> "Keyboard"
            textToMap.contains("mouse") || textToMap.contains("computer mouse") -> "Mouse"
            textToMap.contains("headphone") || textToMap.contains("earphone") -> "Headphones"
            textToMap.contains("backpack") || textToMap.contains("back pack") -> "Backpack"
            textToMap.contains("running_shoe") || textToMap.contains("shoe") || textToMap.contains("sneaker") -> "Shoes"
            textToMap.contains("cellular") || textToMap.contains("cellphone") || textToMap.contains("mobile phone") || textToMap.contains("telephone") -> "Mobile Phone"
            textToMap.contains("remote") || textToMap.contains("remote_control") -> "Remote Control"
            textToMap.contains("ballpoint") || textToMap.contains("pen") -> "Pen"
            textToMap.contains("wallet") || textToMap.contains("billfold") -> "Wallet"
            textToMap.contains("charger") || textToMap.contains("adapter") -> "Charger"
            textToMap.contains("key") -> "Keys"
            textToMap.contains("glasses") || textToMap.contains("spectacles") || textToMap.contains("sunglass") -> "Glasses"
            textToMap.contains("watch") || textToMap.contains("digital watch") || textToMap.contains("stopwatch") -> "Watch"
            textToMap.contains("umbrella") -> "Umbrella"
            textToMap.contains("clothes") || textToMap.contains("jersey") || textToMap.contains("shirt") -> "Clothing"
            textToMap.contains("bottle") -> "Water Bottle"
            textToMap.contains("home_goods") || textToMap.contains("home goods") -> ""
            textToMap.contains("food") || textToMap.contains("place") || textToMap.contains("plant") -> ""
            else -> {
                textToMap.replace("_", " ").split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
            }
        }
    }
}
