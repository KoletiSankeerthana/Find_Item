package com.wheredidikeepit.app.utils.engine

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class MlKitBaseRecognitionEngine : ItemRecognitionEngine {
    override val name: String = "MODEL_A (MLKit Base)"

    override suspend fun recognize(context: Context, photoUri: Uri): List<RecognitionResult> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            try {
                val options = ImageLabelerOptions.Builder()
                    .setConfidenceThreshold(0.20f)
                    .build()
                val labeler = ImageLabeling.getClient(options)
                val inputImage = InputImage.fromFilePath(context, photoUri)

                labeler.process(inputImage)
                    .addOnSuccessListener { labels ->
                        val results = labels.map { label ->
                            Log.d("ML_DEBUG", "ENGINE: $name | LABEL: ${label.text} | CONFIDENCE: ${label.confidence} | INDEX: ${label.index}")
                            RecognitionResult(
                                label = label.text,
                                confidence = label.confidence,
                                sourceEngine = name
                            )
                        }
                        labeler.close()
                        if (continuation.isActive) {
                            continuation.resume(results)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ML_DEBUG", "ENGINE: $name FAILED: ${e.message}")
                        labeler.close()
                        if (continuation.isActive) {
                            continuation.resume(emptyList())
                        }
                    }
            } catch (e: Exception) {
                Log.e("ML_DEBUG", "ENGINE: $name EXCEPTION: ${e.message}")
                if (continuation.isActive) {
                    continuation.resume(emptyList())
                }
            }
        }
    }
}
