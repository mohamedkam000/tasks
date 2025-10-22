package com.app.tasks.ui.pages.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.tasks.R
import com.app.tasks.constants.Constants
import com.app.tasks.logic.database.TasksEntity
import com.app.tasks.logic.datastore.DataStoreManager
import com.app.tasks.ui.TasksDefaults
import com.app.tasks.ui.components.AnimatedExtendedFloatingActionButton
import com.app.tasks.ui.components.ChipItem
import com.app.tasks.ui.components.ConfirmDialog
import com.app.tasks.ui.components.LargeTopAppBarScaffold
import com.app.tasks.ui.pages.editor.components.TasksCategoryChip
import com.app.tasks.ui.pages.editor.components.TasksCategoryTextField
import com.app.tasks.ui.pages.editor.components.TasksContentTextField
import com.app.tasks.ui.pages.editor.components.TasksPrioritySlider
import com.app.tasks.ui.pages.editor.state.rememberEditorState
import com.app.tasks.utils.VibrationUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun TasksEditorPage(
    modifier: Modifier = Modifier,
    toDo: TasksEntity? = null,
    onSave: (TasksEntity) -> Unit,
    onDelete: () -> Unit,
    onNavigateUp: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val view = LocalView.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val uiState = rememberEditorState(initialTodo = toDo)

    val originalCategories by DataStoreManager.categoriesFlow.collectAsState(initial = emptyList())
    val categories = originalCategories
        .mapIndexed { index, category ->
            ChipItem(
                id = index,
                name = category
            )
        } + ChipItem(id = -1, name = stringResource(R.string.label_customization))

    var defaultIndex by remember { mutableIntStateOf(-1) }
    LaunchedEffect(originalCategories, toDo) {
        if (originalCategories.isEmpty()) return@LaunchedEffect
        if (toDo == null) {
            val index = if (categories.size == 1) -1 else 0
            defaultIndex = index
            uiState.selectedCategoryIndex = index
        } else {
            val index = categories.firstOrNull { it.name == toDo.category }?.id ?: -1
            defaultIndex = index
            uiState.selectedCategoryIndex = index
        }
    }

    val isCustomCategory by remember {
        derivedStateOf {
            uiState.selectedCategoryIndex == -1
        }
    }

    fun checkModifiedBeforeBack() {
        if (uiState.isModified()) {
            uiState.showExitConfirmDialog = true
        } else {
            onNavigateUp()
        }
    }

    BackHandler { checkModifiedBeforeBack() }

    LargeTopAppBarScaffold(
        title = stringResource(if (toDo != null) R.string.title_edit_task else R.string.action_add_task),
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            with(sharedTransitionScope) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (toDo !== null) {
                        AnimatedExtendedFloatingActionButton(
                            icon = Icons.Outlined.Delete,
                            text = stringResource(R.string.action_delete),
                            expanded = true,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            onClick = { uiState.showDeleteConfirmDialog = true },
                            modifier = Modifier.imePadding()
                        )
                    }
                    AnimatedExtendedFloatingActionButton(
                        icon = Icons.Outlined.Save,
                        text = stringResource(R.string.action_save),
                        expanded = true,
                        onClick = {
                            if (uiState.setErrorIfNotValid()) {
                                return@AnimatedExtendedFloatingActionButton
                            } else {
                                uiState.clearError()
                                val newTodo = TasksEntity(
                                    id = toDo?.id ?: 0,
                                    content = uiState.toDoContent,
                                    category = if (isCustomCategory) uiState.categoryContent else categories[uiState.selectedCategoryIndex].name,
                                    priority = uiState.priorityState,
                                    isCompleted = uiState.isCompleted
                                )
                                onSave(newTodo)
                            }
                        },
                        modifier = Modifier
                            .imePadding()
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = Constants.KEY_TASKS_FAB_TRANSITION),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }
        },
        onBack = { checkModifiedBeforeBack() },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = TasksDefaults.screenPadding)
                .fillMaxSize()
        ) {
            item {
                TasksContentTextField(
                    value = uiState.toDoContent,
                    onValueChange = { uiState.toDoContent = it },
                    isError = uiState.isErrorContent,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            item {
                Text(
                    text = stringResource(R.string.label_category),
                    style = MaterialTheme.typography.titleMedium
                )

                TasksCategoryChip(
                    items = categories,
                    defaultSelectedItemIndex = defaultIndex,
                    isLoading = originalCategories.isEmpty(),
                    onCategorySelected = { uiState.selectedCategoryIndex = it },
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedVisibility(
                    visible = isCustomCategory,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    TasksCategoryTextField(
                        value = uiState.categoryContent,
                        onValueChange = { uiState.categoryContent = it },
                        isError = uiState.isErrorCategory,
                        supportingText = stringResource(uiState.categorySupportingText),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.label_priority),
                    style = MaterialTheme.typography.titleMedium
                )

                TasksPrioritySlider(
                    value = { uiState.priorityState },
                    onValueChange = { uiState.priorityState = it },
                )
            }

            item {
                if (toDo != null) {
                    Text(
                        text = stringResource(R.string.label_more),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.tip_mark_completed),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Switch(
                            checked = uiState.isCompleted,
                            onCheckedChange = {
                                VibrationUtils.performHapticFeedback(view)
                                uiState.isCompleted = it
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.size(80.dp))
            }
        }

        ConfirmDialog(
            visible = uiState.showExitConfirmDialog,
            icon = Icons.AutoMirrored.Outlined.Undo,
            text = stringResource(R.string.tip_discard_changes),
            onConfirm = {
                uiState.showExitConfirmDialog = false
                onNavigateUp()
            },
            onDismiss = { uiState.showExitConfirmDialog = false }
        )

        ConfirmDialog(
            visible = uiState.showDeleteConfirmDialog,
            icon = Icons.Outlined.Delete,
            text = stringResource(R.string.tip_delete_task, 1),
            onConfirm = onDelete,
            onDismiss = { uiState.showDeleteConfirmDialog = false }
        )
    }
}