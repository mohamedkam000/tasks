package com.app.tasks.ui.pages.settings.components.appearance.contrast

import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.app.tasks.R
import com.app.tasks.ui.pages.settings.components.MoreContentSettingsItem
import com.app.tasks.ui.theme.ContrastLevel
import com.app.tasks.utils.VibrationUtils

@Composable
fun ContrastPicker(
    currentContrast: ContrastLevel,
    onContrastChange: (ContrastLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val context = LocalContext.current
    MoreContentSettingsItem(
        title = stringResource(R.string.pref_contrast_level),
        description = stringResource(R.string.pref_contrast_level_desc),
        modifier = modifier
    ) {
        val contrastLevelName =
            ContrastLevel.entries.map { it.getDisplayName(context) }
        var lastVibratedLevel by remember { mutableFloatStateOf(currentContrast.value) }

        Slider(
            modifier = Modifier.semantics {
                contentDescription =
                    context.getString(R.string.pref_contrast_level) + contrastLevelName[currentContrast.ordinal]
                stateDescription = contrastLevelName[currentContrast.ordinal]
                liveRegion = LiveRegionMode.Polite
            },
            value = currentContrast.value,
            onValueChange = { newValue ->
                onContrastChange(ContrastLevel.fromFloat(newValue))

                if (newValue != lastVibratedLevel) {
                    VibrationUtils.performHapticFeedback(
                        view,
                        HapticFeedbackConstants.LONG_PRESS
                    )
                    Log.d("ContrastPicker", "Level changed to: ${newValue}")
                    lastVibratedLevel = newValue
                }
            },
            valueRange = -1f..1f,
            steps = 3,
        )

        Spacer(Modifier.size(5.dp))

        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearAndSetSemantics {},
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.contrast_very_low))
                Text(stringResource(R.string.contrast_low))
                Text(stringResource(R.string.contrast_default))
                Text(stringResource(R.string.contrast_high))
                Text(stringResource(R.string.contrast_very_high))
            }
        }
    }
}