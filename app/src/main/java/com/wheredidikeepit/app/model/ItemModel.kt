package com.wheredidikeepit.app.model

data class ItemModel(
    val id: Long = 0,
    val name: String,
    val location: String,
    val notes: String = "",
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
