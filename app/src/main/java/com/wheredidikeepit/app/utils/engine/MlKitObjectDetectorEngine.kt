package com.wheredidikeepit.app.utils.engine

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class MlKitObjectDetectorEngine : ItemRecognitionEngine {
    override val name: String = "MODEL_B (MLKit Object Detector)"

    override suspend fun recognize(context: Context, photoUri: Uri): List<RecognitionResult> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            try {
                val options = ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                    .enableMultipleObjects()
                    .enableClassification()
                    .build()

                val detector = ObjectDetection.getClient(options)
                val inputImage = InputImage.fromFilePath(context, photoUri)

                detector.process(inputImage)
                    .addOnSuccessListener { detectedObjects ->
                        val results = mutableListOf<RecognitionResult>()
                        for (obj in detectedObjects) {
                            for (label in obj.labels) {
                                Log.d("ML_DEBUG", "ENGINE: $name | LABEL: ${label.text} | CONFIDENCE: ${label.confidence} | INDEX: ${label.index} | BBOX: ${obj.boundingBox}")
                                results.add(
                                    RecognitionResult(
                                        label = label.text,
                                        confidence = label.confidence,
                                        boundingBox = obj.boundingBox,
                                        sourceEngine = name
                                    )
                                )
                            }
                        }
                        detector.close()
                        if (continuation.isActive) {
                            continuation.resume(results)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ML_DEBUG", "ENGINE: $name FAILED: ${e.message}")
                        detector.close()
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
