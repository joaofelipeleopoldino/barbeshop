package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Signature Luxury Barbershop Dark Theme
private val BarbershopDarkColorScheme = darkColorScheme(
    primary = BarberGold,
    onPrimary = Color(0xFF1A1202),
    primaryContainer = BarberGoldContainer,
    onPrimaryContainer = BarberOnGoldContainer,
    secondary = BarberGoldLight,
    onSecondary = Color(0xFF1A1202),
    secondaryContainer = Color(0xFF33291B),
    onSecondaryContainer = Color(0xFFF8E7C5),
    tertiary = BarberBluePole,
    onTertiary = Color.White,
    background = BarberDarkBg,
    onBackground = BarberTextPrimary,
    surface = BarberDarkSurface,
    onSurface = BarberTextPrimary,
    surfaceVariant = BarberDarkSurfaceVariant,
    onSurfaceVariant = BarberTextSecondary,
    outline = BarberBorder,
    outlineVariant = Color(0xFF2E2A26),
    error = BarberRed,
    onError = Color.White
)

private val BarbershopLightColorScheme = lightColorScheme(
    primary = BarberGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFBE7C3),
    onPrimaryContainer = Color(0xFF2C1E03),
    secondary = Color(0xFF8A6520),
    onSecondary = Color.White,
    background = Color(0xFFFBF9F6),
    onBackground = Color(0xFF1C1A17),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1A17),
    surfaceVariant = Color(0xFFEFECE6),
    onSurfaceVariant = Color(0xFF555048),
    outline = Color(0xFFD6D0C5),
    error = BarberRed,
    onError = Color.White
)

@Composable
fun BarberProTheme(
    darkTheme: Boolean = true, // Default to stunning dark luxury barbershop theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) BarbershopDarkColorScheme else BarbershopLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
