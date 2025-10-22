package com.app.tasks.ui.pages.settings.components.appearance.palette

import android.os.Build
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.app.tasks.ui.theme.ContrastLevel
import com.app.tasks.ui.theme.PaletteStyle
import com.app.tasks.ui.theme.dynamicColorScheme
import com.app.tasks.utils.VibrationUtils

@Composable
fun PaletteItem(
    isDynamicColor: Boolean,
    isDark: Boolean,
    paletteStyle: PaletteStyle,
    contrastLevel: ContrastLevel,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .width(90.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(
                role = Role.Button,
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onSelect()
                }
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MaterialTheme(
            colorScheme = dynamicColorScheme(
                keyColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDynamicColor) {
                    colorResource(id = android.R.color.system_accent1_500)
                } else {
                    Color(0xFF0061A4)
                },
                isDark = isDark,
                contrastLevel = contrastLevel.value.toDouble(),
                style = paletteStyle
            )
        ) {
            val borderWidth by animateDpAsState(if (selected) 3.dp else (-1).dp)
            Column(
                modifier = Modifier
                    .width(70.dp)
                    .clip(MaterialTheme.shapes.large)
                    .border(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.large
                    ),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.primaryContainer,
                ).fastForEach {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(it)
                    )
                }
            }
        }

        Spacer(Modifier.size(8.dp))

        Text(
            text = paletteStyle.getDisplayName(context),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}