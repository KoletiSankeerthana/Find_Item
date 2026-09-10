package com.wheredidikeepit.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import com.wheredidikeepit.app.ui.theme.BorderColor
import com.wheredidikeepit.app.ui.theme.DeepPlumMainText
import com.wheredidikeepit.app.ui.theme.MutedPlum
import com.wheredidikeepit.app.ui.theme.MutedTextGray
import com.wheredidikeepit.app.ui.theme.SecondaryTextGray
import com.wheredidikeepit.app.ui.theme.SurfaceWhite

@Composable
fun HomeHeader(
    activeProfile: String = "Default",
    profiles: List<String> = listOf("Default"),
    onProfileSelected: (String) -> Unit = {},
    onAddProfileClick: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBackClick: (() -> Unit)? = null
) {
    var expandedMenu by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onBackClick?.invoke()
                },
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = DeepPlumMainText
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Where Did I Keep It? 📍",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DeepPlumMainText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Remember where. Find when. ✨",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextGray
                )
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = MutedPlum
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // White Light Surface Profile Selector Pill
        Box {
            Surface(
                onClick = { expandedMenu = true },
                shape = RoundedCornerShape(20.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderColor),
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "👤", style = MaterialTheme.typography.labelMedium)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Using as: $activeProfile",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = DeepPlumMainText
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = "Switch profile",
                        tint = MutedPlum
                    )
                }
            }

            DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false }
            ) {
                profiles.forEach { profile ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = profile,
                                color = DeepPlumMainText,
                                fontWeight = if (profile == activeProfile) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            expandedMenu = false
                            onProfileSelected(profile)
                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                tint = MutedPlum,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add another profile",
                                color = MutedPlum,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    onClick = {
                        expandedMenu = false
                        showAddDialog = true
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AddProfileDialog(
            profiles = profiles,
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                showAddDialog = false
                onAddProfileClick(name)
            }
        )
    }
}

@Composable
private fun AddProfileDialog(
    profiles: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var showDuplicateError by remember { mutableStateOf(false) }

    if (showDuplicateError) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDuplicateError = false },
            title = {
                Text(
                    text = "Account already exists ⚠️",
                    fontWeight = FontWeight.Bold,
                    color = DeepPlumMainText
                )
            },
            text = {
                Text(
                    text = "An account with this name already exists.\n\nPlease use another name.",
                    color = SecondaryTextGray
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDuplicateError = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MutedPlum)
                ) {
                    Text("OK", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Add Profile 😊",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepPlumMainText
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Enter profile name", color = MutedTextGray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MutedPlum,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = DeepPlumMainText,
                        unfocusedTextColor = DeepPlumMainText,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel", color = SecondaryTextGray, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val trimmed = text.trim()
                            if (trimmed.isNotEmpty()) {
                                val isDuplicate = profiles.any { it.equals(trimmed, ignoreCase = true) }
                                if (isDuplicate) {
                                    showDuplicateError = true
                                } else {
                                    onConfirm(trimmed)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MutedPlum,
                            contentColor = SurfaceWhite
                        )
                    ) {
                        Text("Add", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
