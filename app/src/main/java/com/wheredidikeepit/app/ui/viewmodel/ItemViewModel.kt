package com.wheredidikeepit.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wheredidikeepit.app.data.local.AppDatabase
import com.wheredidikeepit.app.data.repository.ItemRepository
import com.wheredidikeepit.app.model.ItemModel
import com.wheredidikeepit.app.utils.ProfileManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository
    val profileManager: ProfileManager = ProfileManager(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val activeProfile: StateFlow<String> = profileManager.activeProfile
    val profiles: StateFlow<List<String>> = profileManager.profiles

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _selectedSort = MutableStateFlow(SortOption.NEWEST)
    val selectedSort: StateFlow<SortOption> = _selectedSort.asStateFlow()

    val allItems: StateFlow<List<ItemModel>>
    val searchedItems: StateFlow<List<ItemModel>>
    val filteredAndSortedItems: StateFlow<List<ItemModel>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ItemRepository(application, database.itemDao())

        allItems = activeProfile
            .flatMapLatest { profile ->
                repository.observeAllItems(profile)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        searchedItems = combine(activeProfile, _searchQuery) { profile, query ->
            Pair(profile, query)
        }
        .flatMapLatest { (profile, query) ->
            repository.searchItems(profile, query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredAndSortedItems = combine(searchedItems, _selectedFilter, _selectedSort) { items, filter, sort ->
            val filtered = when {
                filter == "With Photo" -> items.filter { !it.photoUri.isNullOrEmpty() }
                filter != "All" && filter.isNotBlank() -> items.filter { it.location.contains(filter, ignoreCase = true) }
                else -> items
            }

            when (sort) {
                SortOption.NEWEST -> filtered.sortedByDescending { it.updatedAt }
                SortOption.OLDEST -> filtered.sortedBy { it.updatedAt }
                SortOption.NAME_ASC -> filtered.sortedBy { it.name.lowercase() }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun setSelectedFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun setSelectedSort(sort: SortOption) {
        _selectedSort.value = sort
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun switchProfile(name: String) {
        profileManager.setActiveProfile(name)
    }

    fun hasProfile(name: String): Boolean {
        return profileManager.hasProfile(name)
    }

    fun createProfile(name: String): Boolean {
        val trimmed = name.trim()
        if (hasProfile(trimmed)) {
            return false
        }
        viewModelScope.launch {
            repository.migrateLegacyItems(trimmed)
        }
        return profileManager.addProfile(trimmed)
    }

    fun observeItemById(id: Long): Flow<ItemModel?> {
        return repository.observeItemById(id)
    }

    fun saveItem(
        name: String,
        location: String,
        notes: String,
        photoUriString: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.insertItem(
                    name = name,
                    location = location,
                    notes = notes,
                    photoUriString = photoUriString,
                    profileId = activeProfile.value
                )
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateItem(
        id: Long,
        name: String,
        location: String,
        notes: String,
        photoUriString: String?,
        createdAt: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateItem(
                    id = id,
                    name = name,
                    location = location,
                    notes = notes,
                    newPhotoUriString = photoUriString,
                    createdAt = createdAt,
                    profileId = activeProfile.value
                )
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteItem(item: ItemModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.deleteItem(item)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class ItemViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class SortOption(val label: String) {
    NEWEST("Newest First"),
    OLDEST("Oldest First"),
    NAME_ASC("Name (A-Z)")
}
