package com.wheredidikeepit.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wheredidikeepit.app.model.ItemModel
import com.wheredidikeepit.app.ui.components.EmptyStateUI
import com.wheredidikeepit.app.ui.components.HomeHeader
import com.wheredidikeepit.app.ui.components.ItemCard
import com.wheredidikeepit.app.ui.components.SearchBarUI
import com.wheredidikeepit.app.ui.theme.BorderColor
import com.wheredidikeepit.app.ui.theme.DeepPlumCharcoalText
import com.wheredidikeepit.app.ui.theme.DeepPlumMainText
import com.wheredidikeepit.app.ui.theme.MutedDeepPlum
import com.wheredidikeepit.app.ui.theme.MutedPlum
import com.wheredidikeepit.app.ui.theme.MutedPlumGrayText
import com.wheredidikeepit.app.ui.theme.SecondaryTextGray
import com.wheredidikeepit.app.ui.theme.SoftLavender
import com.wheredidikeepit.app.ui.theme.SurfaceWhite
import com.wheredidikeepit.app.ui.viewmodel.SortOption

@Composable
fun HomeScreen(
    items: List<ItemModel> = emptyList(),
    allItemsList: List<ItemModel> = emptyList(),
    totalItemsCount: Int = 0,
    activeProfile: String = "Default",
    profiles: List<String> = listOf("Default"),
    searchQuery: String = "",
    selectedFilter: String = "All",
    selectedSort: SortOption = SortOption.NEWEST,
    onSearchQueryChange: (String) -> Unit = {},
    onFilterSelected: (String) -> Unit = {},
    onSortSelected: (SortOption) -> Unit = {},
    onProfileSelected: (String) -> Unit = {},
    onAddProfileClick: (String) -> Unit = {},
    onNavigateToAddItem: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onItemClick: (ItemModel) -> Unit = {},
    onBackClick: (() -> Unit)? = null
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    // Dynamically extract top location categories from user's items for quick filter chips
    val locationCategories = remember(allItemsList) {
        allItemsList.map { item ->
            item.location.split("→").firstOrNull()?.trim() ?: item.location.trim()
        }.filter { it.isNotBlank() }.distinct().take(6)
    }

    // Dynamic suggestions based on item names and locations
    val searchSuggestions = remember(searchQuery, allItemsList) {
        if (searchQuery.isNotBlank()) {
            allItemsList.map { it.name }.filter {
                it.contains(searchQuery, ignoreCase = true) && !it.equals(searchQuery, ignoreCase = true)
            }.distinct().take(3)
        } else {
            emptyList()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (totalItemsCount > 0 || searchQuery.isNotBlank() || selectedFilter != "All") {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToAddItem,
                    icon = { Text(text = "＋", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary) },
                    text = { Text("Add Item", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
                    containerColor = MutedDeepPlum,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                HomeHeader(
                    activeProfile = activeProfile,
                    profiles = profiles,
                    onProfileSelected = onProfileSelected,
                    onAddProfileClick = onAddProfileClick,
                    onSettingsClick = onNavigateToSettings,
                    onBackClick = onBackClick
                )
            }

            item {
                SearchBarUI(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange
                )
            }

            // Search Autocomplete / Suggestion Chips
            if (searchSuggestions.isNotEmpty()) {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchSuggestions) { suggestion ->
                            Surface(
                                onClick = { onSearchQueryChange(suggestion) },
                                shape = RoundedCornerShape(14.dp),
                                color = SoftLavender,
                                border = null
                            ) {
                                Text(
                                    text = "💡 $suggestion",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DeepPlumMainText,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Filter Chips and Sort Selection Bar (When items exist or filter is active)
            if (totalItemsCount > 0 || searchQuery.isNotBlank() || selectedFilter != "All") {
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedFilter == "All",
                                    onClick = { onFilterSelected("All") },
                                    label = { Text("All ($totalItemsCount)") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MutedPlum,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = SurfaceWhite,
                                        labelColor = DeepPlumMainText
                                    )
                                )
                            }

                            item {
                                FilterChip(
                                    selected = selectedFilter == "With Photo",
                                    onClick = {
                                        onFilterSelected(if (selectedFilter == "With Photo") "All" else "With Photo")
                                    },
                                    label = { Text("📷 With Photo") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MutedPlum,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = SurfaceWhite,
                                        labelColor = DeepPlumMainText
                                    )
                                )
                            }

                            items(locationCategories) { loc ->
                                FilterChip(
                                    selected = selectedFilter == loc,
                                    onClick = {
                                        onFilterSelected(if (selectedFilter == loc) "All" else loc)
                                    },
                                    label = { Text("📍 $loc") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MutedPlum,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = SurfaceWhite,
                                        labelColor = DeepPlumMainText
                                    )
                                )
                            }
                        }

                        // Sorting Controls Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    searchQuery.isNotBlank() -> "Search Results (${items.size} found)"
                                    selectedFilter != "All" -> "Filtered ($selectedFilter) • ${items.size} items"
                                    else -> "Your Things 📦 ($totalItemsCount saved)"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlumCharcoalText
                            )

                            Box {
                                Surface(
                                    onClick = { sortMenuExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    color = SurfaceWhite,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Sort: ${selectedSort.label} ▾",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = SecondaryTextGray
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = sortMenuExpanded,
                                    onDismissRequest = { sortMenuExpanded = false }
                                ) {
                                    SortOption.values().forEach { sortOpt ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = sortOpt.label,
                                                    fontWeight = if (selectedSort == sortOpt) FontWeight.Bold else FontWeight.Normal,
                                                    color = DeepPlumMainText
                                                )
                                            },
                                            onClick = {
                                                onSortSelected(sortOpt)
                                                sortMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (totalItemsCount == 0 && searchQuery.isBlank() && selectedFilter == "All") {
                item {
                    EmptyStateUI(onAddItemClick = onNavigateToAddItem)
                }
            } else if (items.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (searchQuery.isNotBlank()) "No items match \"$searchQuery\"" else "No items found for filter \"$selectedFilter\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MutedPlumGrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            onClick = {
                                onSearchQueryChange("")
                                onFilterSelected("All")
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = SoftLavender
                        ) {
                            Text(
                                text = "Clear search & filters",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlumMainText,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            } else {
                items(
                    items = items,
                    key = { item -> item.id }
                ) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}
