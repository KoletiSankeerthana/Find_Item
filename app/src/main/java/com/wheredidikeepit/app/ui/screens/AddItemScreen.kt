package com.wheredidikeepit.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wheredidikeepit.app.model.ItemModel
import com.wheredidikeepit.app.ui.components.CustomLocationBuilder
import com.wheredidikeepit.app.ui.components.LocationUtils
import com.wheredidikeepit.app.ui.components.MlAnalysisState
import com.wheredidikeepit.app.ui.components.PhotoSectionCard
import com.wheredidikeepit.app.ui.components.SuggestionChipsUI
import com.wheredidikeepit.app.ui.theme.BorderGray
import com.wheredidikeepit.app.ui.theme.DeepPlumCharcoalText
import com.wheredidikeepit.app.ui.theme.HintGrayText
import com.wheredidikeepit.app.ui.theme.MutedDeepPlum
import com.wheredidikeepit.app.ui.theme.MutedPlumGrayText
import com.wheredidikeepit.app.ui.theme.WhereDidIKeepItTheme
import com.wheredidikeepit.app.utils.ImageMLUtils
import com.wheredidikeepit.app.utils.PhotoUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    onBackClick: () -> Unit = {},
    onItemSaved: (ItemModel) -> Unit = {}
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

    // Trigger ML Kit Image Labeling when photoUri changes
    LaunchedEffect(photoUri) {
        val currentUri = photoUri
        if (currentUri != null) {
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
        } else {
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
                    Column {
                        Text(
                            text = "Add something 📦",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlumCharcoalText
                        )
                        Text(
                            text = "Save it now. Find it later. ✨",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedPlumGrayText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = MutedDeepPlum
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = DeepPlumCharcoalText
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
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
                    text = "What are you saving? *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = DeepPlumCharcoalText
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
                            text = "e.g. Water bottle, charger, passport",
                            style = MaterialTheme.typography.bodyMedium,
                            color = HintGrayText
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
                        focusedBorderColor = MutedDeepPlum,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = DeepPlumCharcoalText,
                        unfocusedTextColor = DeepPlumCharcoalText,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
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
                    text = "Anything else to remember? 📝",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = DeepPlumCharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        if (it.length <= 250) notes = it
                    },
                    placeholder = {
                        Text(
                            text = "e.g. Behind the steel containers",
                            style = MaterialTheme.typography.bodyMedium,
                            color = HintGrayText
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MutedDeepPlum,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = DeepPlumCharcoalText,
                        unfocusedTextColor = DeepPlumCharcoalText,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons Section (Cancel & Save)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, MutedDeepPlum),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MutedDeepPlum
                    )
                }

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
                            val finalLocation = LocationUtils.buildLocationString(locationLevels)
                            val newItem = ItemModel(
                                name = itemName.trim(),
                                location = finalLocation,
                                notes = notes.trim(),
                                photoUri = photoUri?.toString()
                            )
                            onItemSaved(newItem)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please check your entries before saving.")
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MutedDeepPlum,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Save Item ✨",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
