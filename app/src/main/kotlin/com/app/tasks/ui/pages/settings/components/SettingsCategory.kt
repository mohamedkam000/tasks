package com.app.tasks.ui.pages.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.app.tasks.ui.TasksDefaults

@Composable
fun SettingsCategory(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = TasksDefaults.settingsItemVerticalPadding / 2,
                start = TasksDefaults.settingsItemHorizontalPadding,
                end = TasksDefaults.settingsItemHorizontalPadding
            )
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}