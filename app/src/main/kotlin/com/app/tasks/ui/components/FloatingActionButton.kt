package com.app.tasks.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.tasks.utils.VibrationUtils

@Composable
fun AnimatedExtendedFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    textOverflow: TextOverflow = TextOverflow.Clip,
    expanded: Boolean,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    onClick: () -> Unit
) {
    val view = LocalView.current
    FloatingActionButton(
        onClick = {
            VibrationUtils.performHapticFeedback(view)
            onClick()
        },
        elevation = elevation,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
            AnimatedVisibility(expanded) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = text,
                        maxLines = 1,
                        overflow = textOverflow
                    )
                }
            }
        }
    }
}