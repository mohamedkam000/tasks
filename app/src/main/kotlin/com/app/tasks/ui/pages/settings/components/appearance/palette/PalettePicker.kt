package com.app.tasks.ui.pages.settings.components.appearance.palette

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.tasks.R
import com.app.tasks.ui.pages.settings.components.LazyRowSettingsItem
import com.app.tasks.ui.theme.ContrastLevel
import com.app.tasks.ui.theme.DarkMode
import com.app.tasks.ui.theme.PaletteStyle

@Composable
fun PalettePicker(
    currentPalette: PaletteStyle,
    onPaletteChange: (paletteStyle: PaletteStyle) -> Unit,
    isDynamicColor: Boolean,
    isDarkMode: DarkMode,
    contrastLevel: ContrastLevel,
    modifier: Modifier = Modifier
) {

    val paletteOptions = remember {
        PaletteStyle.entries.toList()
    }

    LazyRowSettingsItem(
        title = stringResource(R.string.pref_palette_style),
        description = stringResource(R.string.pref_palette_style_desc),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        fadedEdgeWidth = 8.dp,
        modifier = modifier
    ) {
        paletteOptions.forEach { paletteStyle ->
            item {
                PaletteItem(
                    isDynamicColor = isDynamicColor,
                    isDark = when (isDarkMode) {
                        DarkMode.FollowSystem -> isSystemInDarkTheme()
                        DarkMode.Light -> false
                        DarkMode.Dark -> true
                    },
                    paletteStyle = paletteStyle,
                    selected = currentPalette == paletteStyle,
                    contrastLevel = contrastLevel,
                    onSelect = { onPaletteChange(paletteStyle) }
                )
            }
        }
    }
}