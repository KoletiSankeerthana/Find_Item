package com.wheredidikeepit.app.utils.engine

import android.content.Context
import android.graphics.Rect
import android.net.Uri

data class RecognitionResult(
    val label: String,
    val confidence: Float,
    val boundingBox: Rect? = null,
    val sourceEngine: String = ""
)

interface ItemRecognitionEngine {
    val name: String
    suspend fun recognize(context: Context, photoUri: Uri): List<RecognitionResult>
}
