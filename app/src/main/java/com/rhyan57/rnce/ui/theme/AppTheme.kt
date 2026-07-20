package com.rhyan57.rnce.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val RncDarkColorScheme = darkColorScheme(
    primary = Color(0xFF5865F2),
    onPrimary = Color.White,
    background = Color(0xFF1E1F22),
    surface = Color(0xFF2B2D31),
    surfaceVariant = Color(0xFF313338),
    onBackground = Color(0xFFF2F3F5),
    onSurface = Color(0xFFF2F3F5),
    error = Color(0xFFED4245),
    errorContainer = Color(0xFF3B1A1B),
    onError = Color(0xFFFFDFDE)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDynamic = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = when {
        isDynamic -> if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> RncDarkColorScheme
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = {
            if (isDynamic) {
                content()
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorScheme.background,
                                    colorScheme.surface
                                )
                            )
                        )
                ) {
                    content()
                }
            }
        }
    )
}
