package com.wheredidikeepit.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wheredidikeepit.app.ui.theme.BorderGray
import com.wheredidikeepit.app.ui.theme.DeepPlumCharcoalText
import com.wheredidikeepit.app.ui.theme.HintGrayText
import com.wheredidikeepit.app.ui.theme.MutedDeepPlum
import com.wheredidikeepit.app.ui.theme.MutedPlumGrayText
import com.wheredidikeepit.app.ui.theme.MutedSage
import com.wheredidikeepit.app.ui.theme.SoftSageContainer

object LocationUtils {
    fun parseLocationString(location: String): List<String> {
        if (location.isBlank()) return listOf("")
        val split = location.split(Regex("""\s*(?:→|->)\s*""")).map { it.trim() }.filter { it.isNotEmpty() }
        return if (split.isEmpty()) listOf("") else split
    }

    fun buildLocationString(levels: List<String>): String {
        return levels.map { it.trim() }.filter { it.isNotEmpty() }.joinToString(" → ")
    }
}

@Composable
fun CustomLocationBuilder(
    levels: List<String>,
    onLevelsChange: (List<String>) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val joinedPreview = LocationUtils.buildLocationString(levels)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Where did you keep it? 📍 *",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = DeepPlumCharcoalText
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Be as specific as you need.",
            style = MaterialTheme.typography.bodySmall,
            color = MutedPlumGrayText
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Example helper card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = SoftSageContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = MutedSage,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Example location hierarchy:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MutedSage
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🏠 Home → Kitchen → 3rd Shelf\n🏢 Office → Desk → Drawer 2\n🚗 Car → Boot → Left Side",
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepPlumCharcoalText.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Level Inputs
        levels.forEachIndexed { index, levelValue ->
            if (index > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "↓",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MutedDeepPlum
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = levelValue,
                    onValueChange = { newValue ->
                        val updated = levels.toMutableList()
                        updated[index] = newValue
                        onLevelsChange(updated)
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Level ${index + 1}", color = DeepPlumCharcoalText)
                    },
                    placeholder = {
                        val placeholderText = when (index) {
                            0 -> "e.g. Home"
                            1 -> "e.g. Kitchen"
                            2 -> "e.g. 3rd Shelf"
                            3 -> "e.g. Drawer 2"
                            else -> "e.g. Detail ${index + 1}"
                        }
                        Text(
                            text = placeholderText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HintGrayText
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MutedDeepPlum,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = DeepPlumCharcoalText,
                        unfocusedTextColor = DeepPlumCharcoalText,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    trailingIcon = {
                        if (levels.size > 1) {
                            IconButton(
                                onClick = {
                                    val updated = levels.toMutableList()
                                    updated.removeAt(index)
                                    onLevelsChange(updated)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Remove level",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // + Add Level Button
        OutlinedButton(
            onClick = {
                onLevelsChange(levels + "")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MutedDeepPlum),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MutedDeepPlum
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = MutedDeepPlum,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "+ Add Location Level",
                fontWeight = FontWeight.Bold,
                color = MutedDeepPlum
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (joinedPreview.isNotEmpty()) SoftSageContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(
                1.dp,
                if (isError) MaterialTheme.colorScheme.error
                else MutedSage.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        contentDescription = null,
                        tint = MutedSage,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LOCATION PREVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MutedSage
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = joinedPreview.ifEmpty { "(Enter location levels above)" },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (joinedPreview.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                    color = if (joinedPreview.isNotEmpty()) DeepPlumCharcoalText
                    else MutedPlumGrayText
                )
            }
        }

        if (isError) {
            Text(
                text = errorMessage ?: "Please enter at least one location level or remove empty levels.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 6.dp)
            )
        }
    }
}
