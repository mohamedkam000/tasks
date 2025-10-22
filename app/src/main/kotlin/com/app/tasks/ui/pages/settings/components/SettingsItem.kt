package com.app.tasks.ui.pages.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.app.tasks.ui.TasksDefaults
import com.app.tasks.utils.VibrationUtils

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    title: String,
    description: String? = null,
    enableClick: Boolean = true,
    onClick: () -> Unit = {}
) {
    SettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = null,
        enableClick = enableClick,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    title: String,
    description: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    enableClick: Boolean = true,
    onClick: () -> Unit = {}
) {
    SettingsItem(
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = TasksDefaults.settingsItemHorizontalPadding),
                )
            }
        },
        title = title,
        description = description,
        trailingContent = trailingContent,
        enableClick = enableClick,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    title: String,
    description: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    enableClick: Boolean = true,
    onClick: () -> Unit = {}
) {
    val view = LocalView.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape)
            .clickable(
                enabled = enableClick,
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onClick()
                }
            )
            .padding(
                horizontal = TasksDefaults.settingsItemHorizontalPadding,
                vertical = TasksDefaults.settingsItemVerticalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            it()
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
            )
            description?.let {
                Text(
                    text = it,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        trailingContent?.let {
            it()
        }
    }
}