package com.app.tasks.ui.pages.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.app.tasks.ui.TasksDefaults
import com.app.tasks.ui.components.BasicDialog
import com.app.tasks.utils.VibrationUtils

@Composable
fun SettingsRadioDialog(
    visible: Boolean,
    title: String,
    currentOptions: SettingsRadioOptions,
    options: List<SettingsRadioOptions>,
    onSelect: (id: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsDialog(
        visible = visible,
        title = title,
        text = {
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .verticalScroll(rememberScrollState())
            ) {
                options.forEach { option ->
                    RadioItem(
                        selected = option == currentOptions,
                        text = option.text,
                        onClick = {
                            onSelect(option.id)
                            onDismiss()
                        }
                    )
                }
            }
        },
        onDismissRequest = onDismiss,
        modifier = modifier
    )
}

@Composable
fun RadioItem(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.large)
            .selectable(
                selected = selected,
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onClick()
                },
                role = Role.RadioButton
            )
            .padding(horizontal = TasksDefaults.screenPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String,
    text: @Composable (() -> Unit)? = null,
    onDismissRequest: () -> Unit = {}
) {
    BasicDialog(
        visible = visible,
        title = { Text(title) },
        text = text,
        confirmButton = {},
        dismissButton = {},
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}