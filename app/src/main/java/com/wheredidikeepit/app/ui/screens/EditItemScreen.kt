package com.wheredidikeepit.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wheredidikeepit.app.model.ItemModel
import com.wheredidikeepit.app.ui.components.CustomLocationBuilder
import com.wheredidikeepit.app.ui.components.LocationUtils
import com.wheredidikeepit.app.ui.components.MlAnalysisState
import com.wheredidikeepit.app.ui.components.PhotoSectionCard
import com.wheredidikeepit.app.ui.components.SuggestionChipsUI
import com.wheredidikeepit.app.utils.ImageMLUtils
import com.wheredidikeepit.app.utils.PhotoUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    item: ItemModel?,
    onBackClick: () -> Unit = {},
    onItemUpdated: (ItemModel) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var itemName by remember { mutableStateOf("") }
    var locationLevels by remember { mutableStateOf(listOf("")) }
    var notes by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    var itemNameError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }
    var locationErrorMessage by remember { mutableStateOf<String?>(null) }

    // ML Analysis State
    var mlState by remember { mutableStateOf(MlAnalysisState.IDLE) }
    var mlSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var lastAnalyzedUri by remember { mutableStateOf<String?>(null) }

    // Pre-fill state when item is passed or updated
    LaunchedEffect(item) {
        if (item != null) {
            itemName = item.name
            locationLevels = LocationUtils.parseLocationString(item.location)
            notes = item.notes
            val initialUri = item.photoUri?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
            photoUri = initialUri
            if (initialUri != null) {
                lastAnalyzedUri = initialUri.toString()
            }
        }
    }

    // Trigger ML Kit Image Labeling when photoUri changes to a new image
    LaunchedEffect(photoUri) {
        val currentUri = photoUri
        if (currentUri != null && currentUri.toString() != lastAnalyzedUri) {
            lastAnalyzedUri = currentUri.toString()
            mlState = MlAnalysisState.ANALYZING
            try {
                val results = ImageMLUtils.analyzeImage(context, currentUri)
                if (results.isNotEmpty()) {
                    mlSuggestions = results
                    mlState = MlAnalysisState.SUCCESS
                } else {
                    mlSuggestions = emptyList()
                    mlState = MlAnalysisState.NO_RESULTS
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mlSuggestions = emptyList()
                mlState = MlAnalysisState.ERROR
            }
        } else if (currentUri == null) {
            mlSuggestions = emptyList()
            mlState = MlAnalysisState.IDLE
        }
    }

    // Camera Capture Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            photoUri = tempCameraUri
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = PhotoUtils.createCameraTempUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Camera permission is needed to take a photo.")
            }
        }
    }

    // Photo Picker Gallery Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->
        if (selectedUri != null) {
            val localUri = PhotoUtils.copyUriToInternalStorage(context, selectedUri)
            if (localUri != null) {
                photoUri = localUri
            } else {
                photoUri = selectedUri
            }
        }
    }

    fun triggerCameraAction() {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val uri = PhotoUtils.createCameraTempUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun triggerGalleryAction() {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Item",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (item == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Item not found",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // Photo Section
            PhotoSectionCard(
                photoUri = photoUri,
                onTakePhotoClick = { triggerCameraAction() },
                onChooseGalleryClick = { triggerGalleryAction() },
                onRemovePhotoClick = { photoUri = null }
            )

            // ML Suggestions Component
            SuggestionChipsUI(
                state = mlState,
                suggestions = mlSuggestions,
                onSuggestionClick = { selectedLabel ->
                    itemName = selectedLabel
                    itemNameError = false
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Item Name Input Section
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Item name *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = itemName,
                    onValueChange = {
                        if (it.length <= 60) {
                            itemName = it
                            if (it.isNotBlank()) itemNameError = false
                        }
                    },
                    placeholder = {
                        Text(
                            text = "What did you keep? (e.g., Water Bottle)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    isError = itemNameError,
                    supportingText = {
                        if (itemNameError) {
                            Text(
                                text = "Item name is required",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Location Builder Section
            CustomLocationBuilder(
                levels = locationLevels,
                onLevelsChange = { updatedLevels ->
                    locationLevels = updatedLevels
                    locationError = false
                    locationErrorMessage = null
                },
                isError = locationError,
                errorMessage = locationErrorMessage
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Written Notes Section (Optional)
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Add a note (Optional)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        if (it.length <= 250) notes = it
                    },
                    placeholder = {
                        Text(
                            text = "Add details that might help you find it later...\ne.g., Blue bottle behind the steel containers.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Changes Button Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Button(
                    onClick = {
                        val nameValid = itemName.isNotBlank()
                        val hasAnyLocation = locationLevels.any { it.isNotBlank() }
                        val hasEmptyLevel = locationLevels.any { it.isBlank() }

                        itemNameError = !nameValid

                        if (!hasAnyLocation) {
                            locationError = true
                            locationErrorMessage = "Please enter at least one location level."
                        } else if (hasEmptyLevel) {
                            locationError = true
                            locationErrorMessage = "Please fill in all location levels or remove empty levels."
                        } else {
                            locationError = false
                            locationErrorMessage = null
                        }

                        if (nameValid && hasAnyLocation && !hasEmptyLevel) {
                            val updatedLocation = LocationUtils.buildLocationString(locationLevels)
                            val updatedItem = item.copy(
                                name = itemName.trim(),
                                location = updatedLocation,
                                notes = notes.trim(),
                                photoUri = photoUri?.toString(),
                                updatedAt = System.currentTimeMillis()
                            )
                            onItemUpdated(updatedItem)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please check your entries before updating.")
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Update Item",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
