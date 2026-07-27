package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NeoBrutalistColorScheme = lightColorScheme(
    primary = GankColors.GankYellow,
    onPrimary = GankColors.Ink,
    primaryContainer = GankColors.Silver,
    onPrimaryContainer = GankColors.Ink,
    secondary = GankColors.NeonBlue,
    onSecondary = GankColors.Ink,
    background = GankColors.Paper,
    onBackground = GankColors.Ink,
    surface = GankColors.White,
    onSurface = GankColors.Ink,
    surfaceVariant = GankColors.Paper,
    onSurfaceVariant = GankColors.Steel,
    error = GankColors.RedAlert,
    onError = GankColors.White,
    outline = GankColors.Ink
)

private val DarkNeoBrutalistColorScheme = darkColorScheme(
    primary = GankColors.GankYellow,
    onPrimary = GankColors.Ink,
    primaryContainer = GankColors.Steel,
    onPrimaryContainer = GankColors.White,
    secondary = GankColors.NeonBlue,
    onSecondary = GankColors.Ink,
    background = Color(0xFF121212),
    onBackground = GankColors.Paper,
    surface = GankColors.DarkPaper,
    onSurface = GankColors.Paper,
    surfaceVariant = Color(0xFF282828),
    onSurfaceVariant = GankColors.Silver,
    error = GankColors.RedAlert,
    onError = GankColors.White,
    outline = GankColors.Silver
)

@Composable
fun GankTeknisiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkNeoBrutalistColorScheme else NeoBrutalistColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

