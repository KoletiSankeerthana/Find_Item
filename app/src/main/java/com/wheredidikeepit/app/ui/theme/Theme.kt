package com.wheredidikeepit.app.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkDeepPlumPrimary,
    onPrimary = OnDarkDeepPlumPrimary,
    primaryContainer = DarkLavenderContainer,
    onPrimaryContainer = DarkWarmWhiteText,
    secondary = DarkSageSecondary,
    onSecondary = OnDarkSageSecondary,
    secondaryContainer = DarkSageContainer,
    onSecondaryContainer = DarkWarmWhiteText,
    tertiary = DustyRose,
    background = DarkPlumBackground,
    onBackground = DarkWarmWhiteText,
    surface = DarkPlumSurface,
    onSurface = DarkWarmWhiteText,
    surfaceVariant = DarkPlumSurfaceVariant,
    onSurfaceVariant = DarkMutedText,
    outline = DarkBorder,
    error = AccessibleDarkRed
)

private val LightColorScheme = lightColorScheme(
    primary = MutedPlum,
    onPrimary = OnMutedPlum,
    primaryContainer = SoftLavender,
    onPrimaryContainer = DeepPlumMainText,
    secondary = SageAccent,
    onSecondary = OnSageAccent,
    secondaryContainer = SoftSage,
    onSecondaryContainer = DeepPlumMainText,
    tertiary = DustyRose,
    background = WarmCreamBackground,
    onBackground = DeepPlumMainText,
    surface = SurfaceWhite,
    onSurface = DeepPlumMainText,
    surfaceVariant = SoftLavender,
    onSurfaceVariant = SecondaryTextGray,
    outline = BorderColor,
    error = AccessibleDarkRed
)

@Composable
fun WhereDidIKeepItTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
