package com.wheredidikeepit.app.ui.navigation

import android.app.Application
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wheredidikeepit.app.ui.screens.AboutScreen
import com.wheredidikeepit.app.ui.screens.AccountOptionsScreen
import com.wheredidikeepit.app.ui.screens.AddItemScreen
import com.wheredidikeepit.app.ui.screens.ChooseAccountScreen
import com.wheredidikeepit.app.ui.screens.CreateAccountScreen
import com.wheredidikeepit.app.ui.screens.EditItemScreen
import com.wheredidikeepit.app.ui.screens.HomeScreen
import com.wheredidikeepit.app.ui.screens.ItemDetailsScreen
import com.wheredidikeepit.app.ui.screens.SettingsScreen
import com.wheredidikeepit.app.ui.screens.WelcomeOnboardingScreen
import com.wheredidikeepit.app.ui.viewmodel.ItemViewModel
import com.wheredidikeepit.app.ui.viewmodel.ItemViewModelFactory
import com.wheredidikeepit.app.ui.viewmodel.SortOption

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object AccountOptions : Screen("account_options")
    object CreateAccount : Screen("create_account")
    object ChooseAccount : Screen("choose_account")
    object Home : Screen("home")
    object AddItem : Screen("add_item")
    object ItemDetails : Screen("item_details/{itemId}") {
        fun createRoute(itemId: Long) = "item_details/$itemId"
    }
    object EditItem : Screen("edit_item/{itemId}") {
        fun createRoute(itemId: Long) = "edit_item/$itemId"
    }
    object Settings : Screen("settings")
    object About : Screen("about")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: ItemViewModel = viewModel(
        factory = ItemViewModelFactory(application)
    )

    val allItems by viewModel.allItems.collectAsStateWithLifecycle()
    val filteredAndSortedItems by viewModel.filteredAndSortedItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeProfile by viewModel.activeProfile.collectAsStateWithLifecycle()
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing))
        }
    ) {
        composable(Screen.Onboarding.route) {
            WelcomeOnboardingScreen(
                onGetStarted = {
                    if (profiles.isEmpty()) {
                        navController.navigate(Screen.CreateAccount.route) {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(Screen.ChooseAccount.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Screen.AccountOptions.route) {
            AccountOptionsScreen(
                onCreateAccountClick = {
                    navController.navigate(Screen.CreateAccount.route) {
                        launchSingleTop = true
                    }
                },
                onAlreadyHaveAccountClick = {
                    if (profiles.isEmpty()) {
                        navController.navigate(Screen.CreateAccount.route) {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(Screen.ChooseAccount.route) {
                            launchSingleTop = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(
                onCheckDuplicate = { name ->
                    viewModel.hasProfile(name)
                },
                onAccountCreated = { name ->
                    viewModel.createProfile(name)
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ChooseAccount.route) {
            ChooseAccountScreen(
                accountsList = profiles,
                onAccountSelected = { name ->
                    viewModel.switchProfile(name)
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onCreateNewAccountClick = {
                    navController.navigate(Screen.CreateAccount.route) {
                        launchSingleTop = true
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                items = filteredAndSortedItems,
                allItemsList = allItems,
                totalItemsCount = allItems.size,
                activeProfile = activeProfile,
                profiles = profiles,
                searchQuery = searchQuery,
                selectedFilter = selectedFilter,
                selectedSort = selectedSort,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onFilterSelected = { viewModel.setSelectedFilter(it) },
                onSortSelected = { viewModel.setSelectedSort(it) },
                onProfileSelected = { profile -> viewModel.switchProfile(profile) },
                onAddProfileClick = { name -> viewModel.createProfile(name) },
                onNavigateToAddItem = {
                    android.util.Log.d("NAVIGATION_DEBUG", "NAVIGATION: Home -> AddItem")
                    navController.navigate(Screen.AddItem.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                },
                onItemClick = { item ->
                    navController.navigate(Screen.ItemDetails.createRoute(item.id)) {
                        launchSingleTop = true
                    }
                },
                onBackClick = {
                    if (profiles.size > 1) {
                        navController.navigate(Screen.ChooseAccount.route) {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(Screen.Onboarding.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Screen.AddItem.route) {
            AddItemScreen(
                onBackClick = {
                    android.util.Log.d("NAVIGATION_DEBUG", "NAVIGATION: AddItem -> Home (popBackStack)")
                    navController.popBackStack()
                },
                onItemSaved = { newItem ->
                    viewModel.saveItem(
                        name = newItem.name,
                        location = newItem.location,
                        notes = newItem.notes,
                        photoUriString = newItem.photoUri,
                        onSuccess = {
                            Toast.makeText(context, "Item saved", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    )
                }
            )
        }

        composable(
            route = Screen.ItemDetails.route,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
            val itemFlow = remember(itemId) { viewModel.observeItemById(itemId) }
            val item by itemFlow.collectAsStateWithLifecycle(initialValue = null)

            ItemDetailsScreen(
                item = item,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(Screen.EditItem.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onDeleteClick = { itemToDelete ->
                    viewModel.deleteItem(
                        item = itemToDelete,
                        onSuccess = {
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    )
                }
            )
        }

        composable(
            route = Screen.EditItem.route,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
            val itemFlow = remember(itemId) { viewModel.observeItemById(itemId) }
            val item by itemFlow.collectAsStateWithLifecycle(initialValue = null)

            EditItemScreen(
                item = item,
                onBackClick = { navController.popBackStack() },
                onItemUpdated = { updatedItem ->
                    viewModel.updateItem(
                        id = updatedItem.id,
                        name = updatedItem.name,
                        location = updatedItem.location,
                        notes = updatedItem.notes,
                        photoUriString = updatedItem.photoUri,
                        createdAt = updatedItem.createdAt,
                        onSuccess = {
                            Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    )
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                totalItemsCount = allItems.size,
                activeProfile = activeProfile,
                onBackClick = { navController.popBackStack() },
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
