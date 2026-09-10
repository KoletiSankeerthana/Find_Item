package com.wheredidikeepit.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wheredidikeepit.app.ui.theme.BorderColor
import com.wheredidikeepit.app.ui.theme.DeepPlumMainText
import com.wheredidikeepit.app.ui.theme.MutedPlum
import com.wheredidikeepit.app.ui.theme.SecondaryTextGray
import com.wheredidikeepit.app.ui.theme.SoftPowderBlue
import com.wheredidikeepit.app.ui.theme.SurfaceWhite

@Composable
fun SearchBarUI(
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        placeholder = {
            Text(
                text = "Search for something you kept...",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextGray
            )
        },
        leadingIcon = {
            Text(text = "🔎", style = MaterialTheme.typography.bodyMedium)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Clear search text",
                    tint = SecondaryTextGray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onQueryChange("") }
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceWhite,
            unfocusedContainerColor = SoftPowderBlue,
            focusedBorderColor = MutedPlum,
            unfocusedBorderColor = BorderColor,
            focusedTextColor = DeepPlumMainText,
            unfocusedTextColor = DeepPlumMainText
        )
    )
}
