package com.wheredidikeepit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wheredidikeepit.app.ui.theme.DeepPlumMainText
import com.wheredidikeepit.app.ui.theme.MutedPlum
import com.wheredidikeepit.app.ui.theme.OnMutedPlum
import com.wheredidikeepit.app.ui.theme.SecondaryTextGray
import com.wheredidikeepit.app.ui.theme.SoftBlush

@Composable
fun EmptyStateUI(
    onAddItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(
                    color = SoftBlush,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✨", style = MaterialTheme.typography.headlineLarge)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nothing saved yet",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = DeepPlumMainText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Let's remember a few things before they disappear from your memory! 😊",
            style = MaterialTheme.typography.bodyMedium,
            color = SecondaryTextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Add a photo, location and note so future-you can find it.",
            style = MaterialTheme.typography.bodySmall,
            color = SecondaryTextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onAddItemClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MutedPlum,
                contentColor = OnMutedPlum
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(48.dp)
        ) {
            Text(
                text = "＋ Add your first item",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = OnMutedPlum
            )
        }
    }
}
