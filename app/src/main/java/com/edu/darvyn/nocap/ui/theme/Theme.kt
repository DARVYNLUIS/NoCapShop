package com.edu.darvyn.nocap.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = Gray800,
    onPrimaryContainer = White,

    secondary = Gray300,
    onSecondary = Black,
    secondaryContainer = Gray700,
    onSecondaryContainer = Gray200,

    tertiary = Gray400,
    onTertiary = Black,
    tertiaryContainer = Gray600,
    onTertiaryContainer = Gray300,

    error = RedError,
    onError = White,
    errorContainer = RedErrorLight,
    onErrorContainer = RedError,

    background = Gray900,
    onBackground = White,

    surface = Gray800,
    onSurface = White,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray300,

    outline = Gray600,
    outlineVariant = Gray700,

    inverseSurface = White,
    inverseOnSurface = Black,
    inversePrimary = Black,

    surfaceTint = White,
    scrim = Black.copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Gray100,
    onPrimaryContainer = Black,

    secondary = Gray700,
    onSecondary = White,
    secondaryContainer = Gray200,
    onSecondaryContainer = Gray800,

    tertiary = Gray600,
    onTertiary = White,
    tertiaryContainer = Gray300,
    onTertiaryContainer = Gray700,

    error = RedError,
    onError = White,
    errorContainer = RedErrorLight,
    onErrorContainer = RedError,

    background = White,
    onBackground = Black,

    surface = White,
    onSurface = Black,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,

    outline = Gray400,
    outlineVariant = Gray300,

    inverseSurface = Gray900,
    inverseOnSurface = White,
    inversePrimary = White,

    surfaceTint = Black,
    scrim = Black.copy(alpha = 0.5f)
)

@Composable
fun NoCapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color deshabilitado para mantener tema minimalista consistente
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
