package com.wheredidikeepit.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items WHERE profileId = :profileId ORDER BY updatedAt DESC")
    fun observeAllItems(profileId: String = "default"): Flow<List<ItemEntity>>

    @Query("""
        SELECT * FROM items 
        WHERE (profileId = :profileId) AND (
            :query = '' 
            OR LOWER(name) LIKE '%' || LOWER(:query) || '%' 
            OR LOWER(location) LIKE '%' || LOWER(:query) || '%' 
            OR LOWER(notes) LIKE '%' || LOWER(:query) || '%'
        )
        ORDER BY updatedAt DESC
    """)
    fun searchItems(profileId: String = "default", query: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun observeItemById(id: Long): Flow<ItemEntity?>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Long): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("UPDATE items SET profileId = :newProfileId WHERE profileId = 'Default' OR profileId = 'default'")
    suspend fun migrateLegacyItems(newProfileId: String)
}
