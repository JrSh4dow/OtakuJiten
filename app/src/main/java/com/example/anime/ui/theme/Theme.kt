package com.example.anime.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryColor,
    secondary = DarkSecondaryColor,
    onPrimary = DarkTextColor,
    onSecondary = DarkTextColor,
    surface = DarkSelectedContainer,
    onSurface = DarkSelectedContent,
    onSurfaceVariant = DarkUnselectedContent,
    onBackground = DarkTextColor,
    error = DarkErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    onPrimary = Color.White,
    onSecondary = Color.White,
    surface = SelectedContainer,
    onSurface = SelectedContent,
    onBackground = TextColor,
    onSurfaceVariant = UnselectedContent,
    error = ErrorColor

)

@Composable
fun AnimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}