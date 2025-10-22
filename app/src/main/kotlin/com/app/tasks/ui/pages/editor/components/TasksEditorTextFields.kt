package com.app.tasks.ui.pages.editor.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.tasks.R

@Composable
fun TasksContentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.placeholder_add_tasks)) },
        isError = isError,
        supportingText = {
            AnimatedVisibility(
                visible = isError,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(stringResource(R.string.error_no_content_entered))
            }
        },
        modifier = modifier
    )
}

@Composable
fun TasksCategoryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    supportingText: String = stringResource(R.string.tip_max_length_5),
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.label_enter_category_name)) },
        isError = isError,
        supportingText = { Text(supportingText) },
        maxLines = 1,
        modifier = modifier
    )
}