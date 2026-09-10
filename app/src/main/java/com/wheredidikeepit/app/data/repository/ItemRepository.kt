package com.wheredidikeepit.app.data.repository

import android.content.Context
import android.net.Uri
import com.wheredidikeepit.app.data.local.ItemDao
import com.wheredidikeepit.app.data.local.ItemEntity
import com.wheredidikeepit.app.model.ItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ItemRepository(
    private val context: Context,
    private val itemDao: ItemDao
) {
    fun observeAllItems(profileId: String): Flow<List<ItemModel>> {
        return itemDao.observeAllItems(profileId).map { entities ->
            entities.map { entity -> entity.toModel() }
        }
    }

    fun searchItems(profileId: String, query: String): Flow<List<ItemModel>> {
        val sanitizedQuery = query.trim()
        return itemDao.searchItems(profileId, sanitizedQuery).map { entities ->
            entities.map { entity -> entity.toModel() }
        }
    }

    fun observeItemById(id: Long): Flow<ItemModel?> {
        return itemDao.observeItemById(id).map { entity ->
            entity?.toModel()
        }
    }

    suspend fun insertItem(
        name: String,
        location: String,
        notes: String,
        photoUriString: String?,
        profileId: String = "default"
    ): Long {
        return withContext(Dispatchers.IO) {
            val persistentPhotoPath = if (!photoUriString.isNullOrEmpty()) {
                savePhotoToAppStorage(Uri.parse(photoUriString))
            } else null

            val entity = ItemEntity(
                name = name,
                location = location,
                notes = notes,
                photoPath = persistentPhotoPath,
                profileId = profileId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            try {
                itemDao.insertItem(entity)
            } catch (e: Exception) {
                persistentPhotoPath?.let { path ->
                    val file = File(path)
                    if (file.exists()) file.delete()
                }
                throw e
            }
        }
    }

    suspend fun updateItem(
        id: Long,
        name: String,
        location: String,
        notes: String,
        newPhotoUriString: String?,
        createdAt: Long,
        profileId: String = "default"
    ) {
        withContext(Dispatchers.IO) {
            val existingEntity = itemDao.getItemById(id) ?: return@withContext
            val oldPhotoPath = existingEntity.photoPath

            val finalPhotoPath = if (newPhotoUriString.isNullOrEmpty()) {
                if (!oldPhotoPath.isNullOrEmpty()) {
                    val oldFile = File(oldPhotoPath)
                    if (oldFile.exists()) oldFile.delete()
                }
                null
            } else if (newPhotoUriString == oldPhotoPath) {
                oldPhotoPath
            } else {
                val newPath = savePhotoToAppStorage(Uri.parse(newPhotoUriString))
                if (!oldPhotoPath.isNullOrEmpty()) {
                    val oldFile = File(oldPhotoPath)
                    if (oldFile.exists()) oldFile.delete()
                }
                newPath
            }

            val updatedEntity = ItemEntity(
                id = id,
                name = name,
                location = location,
                notes = notes,
                photoPath = finalPhotoPath,
                profileId = profileId,
                createdAt = createdAt,
                updatedAt = System.currentTimeMillis()
            )

            itemDao.updateItem(updatedEntity)
        }
    }

    suspend fun deleteItem(item: ItemModel) {
        withContext(Dispatchers.IO) {
            itemDao.deleteItemById(item.id)

            item.photoUri?.let { path ->
                val file = File(path)
                if (file.exists()) file.delete()
            }
        }
    }

    suspend fun migrateLegacyItems(newProfileId: String) {
        withContext(Dispatchers.IO) {
            itemDao.migrateLegacyItems(newProfileId)
        }
    }

    private fun savePhotoToAppStorage(sourceUri: Uri): String? {
        if (sourceUri.scheme == "file" || sourceUri.path?.startsWith(context.filesDir.absolutePath) == true) {
            val existingFile = File(sourceUri.path ?: "")
            if (existingFile.exists()) {
                return existingFile.absolutePath
            }
        }

        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(Date())
            val photosDir = File(context.filesDir, "item_photos")
            if (!photosDir.exists()) photosDir.mkdirs()

            val targetFile = File(photosDir, "item_photo_${timeStamp}.jpg")

            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                targetFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return null

            targetFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun ItemEntity.toModel(): ItemModel {
        return ItemModel(
            id = this.id,
            name = this.name,
            location = this.location,
            notes = this.notes,
            photoUri = this.photoPath,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
