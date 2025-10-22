package com.app.tasks.ui.pages.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowWidthSizeClass
import com.app.tasks.R
import com.app.tasks.constants.Constants
import com.app.tasks.logic.database.TasksEntity
import com.app.tasks.logic.datastore.DataStoreManager
import com.app.tasks.ui.components.AnimatedExtendedFloatingActionButton
import com.app.tasks.ui.components.ConfirmDialog
import com.app.tasks.ui.pages.main.components.TasksTopAppBar
import com.app.tasks.ui.viewmodels.MainViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainPage(
    viewModel: MainViewModel,
    toTodoEditPage: () -> Unit,
    toSettingsPage: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val toDos = viewModel.sortedTodos.collectAsState(initial = emptyList())
    val selectedTodos = viewModel.selectedTodoIds.collectAsState()
    val showCompleted by DataStoreManager.showCompletedFlow.collectAsState(initial = Constants.PREF_SHOW_COMPLETED_DEFAULT)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val listState = rememberLazyListState()
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val selectedTodoIds by remember { derivedStateOf { selectedTodos.value } }
    val inSelectedMode by remember { derivedStateOf { !selectedTodoIds.isEmpty() } }
    val toDoList by remember { derivedStateOf { toDos.value } }
    val totalTasks by remember { derivedStateOf { toDoList.size } }
    val completedTasks by remember { derivedStateOf { toDoList.count { it.isCompleted } } }
    val filteredTodoList =
        if (showCompleted) toDoList else toDoList.filter { item -> !item.isCompleted }

    BackHandler(inSelectedMode) { viewModel.clearAllTodoSelection() }

    Scaffold(
        topBar = {
            TasksTopAppBar(
                selectedTodoIds = selectedTodoIds,
                selectedMode = inSelectedMode,
                onCancelSelect = { viewModel.clearAllTodoSelection() },
                onSelectAll = { viewModel.selectAllTodos() },
                onDeleteSelectedTodo = { showDeleteConfirmDialog = true },
                toSettingsPage = toSettingsPage
            )
        },
        floatingActionButton = {
            with(sharedTransitionScope) {
                AnimatedVisibility(
                    visible = !inSelectedMode,
                    enter = fadeIn() + expandIn(),
                    exit = shrinkOut() + fadeOut()
                ) {
                    AnimatedExtendedFloatingActionButton(
                        icon = Icons.Outlined.Add,
                        text = stringResource(R.string.action_add_task),
                        expanded = true,
                        onClick = {
                            viewModel.setEditTodoItem(null)
                            toTodoEditPage()
                        },
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = Constants.KEY_TASKS_FAB_TRANSITION),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
    ) { innerPadding ->
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            Column(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                ProgressFragment(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )

                ManagerFragment(
                    state = listState,
                    list = filteredTodoList,
                    onItemClick = { item ->
                        if (inSelectedMode) {
                            viewModel.toggleTodoSelection(item)
                        } else {
                            viewModel.setEditTodoItem(item)
                            toTodoEditPage()
                        }
                    },
                    onItemLongClick = { viewModel.toggleTodoSelection(it) },
                    onItemChecked = { item ->
                        item.apply {
                            viewModel.updateTodo(
                                TasksEntity(
                                    content = content,
                                    category = category,
                                    isCompleted = true,
                                    priority = priority,
                                    id = id
                                )
                            )
                            viewModel.playConfetti()
                        }
                    },
                    selectedTodoIds = selectedTodoIds,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        } else {
            Row(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                ProgressFragment(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )
                ManagerFragment(
                    state = listState,
                    list = filteredTodoList,
                    onItemClick = { item ->
                        if (inSelectedMode) {
                            viewModel.toggleTodoSelection(item)
                        } else {
                            viewModel.setEditTodoItem(item)
                            toTodoEditPage()
                        }
                    },
                    onItemLongClick = { viewModel.toggleTodoSelection(it) },
                    onItemChecked = { item ->
                        item.apply {
                            viewModel.updateTodo(
                                TasksEntity(
                                    content = content,
                                    category = category,
                                    isCompleted = true,
                                    priority = priority,
                                    id = id
                                )
                            )
                            viewModel.playConfetti()
                        }
                    },
                    selectedTodoIds = selectedTodoIds,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        }
        ConfirmDialog(
            visible = showDeleteConfirmDialog,
            icon = Icons.Outlined.Delete,
            text = stringResource(R.string.tip_delete_task, selectedTodoIds.size),
            onConfirm = { viewModel.deleteSelectedTodo() },
            onDismiss = { showDeleteConfirmDialog = false }
        )
    }
}