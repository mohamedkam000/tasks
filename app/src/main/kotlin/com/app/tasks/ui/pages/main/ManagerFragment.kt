package com.app.tasks.ui.pages.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.tasks.R
import com.app.tasks.logic.database.TasksEntity
import com.app.tasks.logic.model.Priority
import com.app.tasks.ui.TasksDefaults
import com.app.tasks.ui.components.LazyColumnCustomScrollBar
import com.app.tasks.ui.pages.main.components.TasksCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ManagerFragment(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list: List<TasksEntity>,
    onItemClick: (TasksEntity) -> Unit = {},
    onItemLongClick: (TasksEntity) -> Unit = {},
    onItemChecked: (TasksEntity) -> Unit = {},
    selectedTodoIds: List<Int>,
) {
    LazyColumnCustomScrollBar(
        state = state,
        modifier = modifier
    ) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(
                start = TasksDefaults.screenPadding,
                bottom = TasksDefaults.toDoCardHeight / 2,
                end = TasksDefaults.screenPadding
            ),
        ) {
            if (list.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.tip_no_task),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(
                    items = list,
                    key = { it.id }
                ) { item ->
                    TasksCard(
                        content = item.content,
                        category = item.category,
                        completed = item.isCompleted,
                        priority = Priority.fromFloat(item.priority),
                        selected = selectedTodoIds.contains(item.id),
                        onCardClick = { onItemClick(item) },
                        onCardLongClick = { onItemLongClick(item) },
                        onChecked = { onItemChecked(item) },
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .animateItem()
                    )
                }
            }
        }
    }
}