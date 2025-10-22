package com.app.tasks.ui.pages.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.tasks.R
import com.app.tasks.logic.model.Priority
import com.app.tasks.ui.TasksDefaults
import com.app.tasks.utils.VibrationUtils

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun TasksCard(
    modifier: Modifier = Modifier,
    content: String,
    category: String,
    completed: Boolean,
    priority: Priority,
    selected: Boolean,
    onCardClick: () -> Unit = {},
    onCardLongClick: () -> Unit = {},
    onChecked: () -> Unit = {},
) {
    val view = LocalView.current
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(TasksDefaults.toDoCardHeight)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = {
                        VibrationUtils.performHapticFeedback(view)
                        onCardClick()
                    },
                    onLongClick = onCardLongClick
                )
                .padding(horizontal = 15.dp)
        ) {
            AnimatedVisibility(selected) {
                Box(
                    Modifier
                        .padding(end = 15.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        tint = contentColorFor(MaterialTheme.colorScheme.secondary),
                        contentDescription = stringResource(R.string.tip_select_this)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = when (priority) {
                                Priority.NotUrgent -> MaterialTheme.colorScheme.surfaceContainerHighest
                                Priority.NotImportant -> MaterialTheme.colorScheme.surfaceContainerHighest
                                Priority.Default -> MaterialTheme.colorScheme.secondary
                                Priority.Important -> MaterialTheme.colorScheme.tertiary
                                Priority.Urgent -> MaterialTheme.colorScheme.error
                            },
                            modifier = Modifier.padding(start = 5.dp)
                        ) {
                            Text(
                                text = priority.getDisplayName(context),
                                textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None,
                            )
                        }
                    }
                ) {
                        Text(
                            text = content,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier
                                .basicMarquee()
                        )
                }

                    Text(
                        text = category.ifEmpty { stringResource(R.string.tip_default_category) },
                        style = MaterialTheme.typography.labelMedium,
                        textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                    )
            }

            AnimatedVisibility(!selected && !completed) {
                IconButton(
                    onClick = {
                        VibrationUtils.performHapticFeedback(view)
                        onChecked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(R.string.tip_mark_completed)
                    )
                }
            }
        }
    }
}