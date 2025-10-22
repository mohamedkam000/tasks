package com.app.tasks.ui.pages.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import com.app.tasks.ui.TasksDefaults
import com.app.tasks.utils.VibrationUtils

@Composable
fun SwitchSettingsItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    leadingIcon: ImageVector? = null,
    title: String,
    description: String? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    val view = LocalView.current
    SettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = {
                    VibrationUtils.performHapticFeedback(view)
                    onCheckedChange(it)
                },
                modifier = Modifier.padding(start = TasksDefaults.settingsItemHorizontalPadding / 2)
            )
        },
        onClick = { onCheckedChange(!checked) },
        modifier = modifier
    )
}