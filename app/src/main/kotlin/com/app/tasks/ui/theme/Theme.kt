package com.app.tasks.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

@Composable
fun TasksTheme(
    color: Color? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contrastLevel: Double = 0.0,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val baseColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && dynamicColor) {
        colorResource(id = android.R.color.system_accent1_500)
    } else {
        Color(0xFF0061A4)
    }

    val keyColor = color ?: baseColor

    val colorScheme = dynamicColorScheme(
        keyColor = keyColor,
        isDark = darkTheme,
        style = style,
        contrastLevel = contrastLevel
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}