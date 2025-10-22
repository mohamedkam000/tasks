package com.app.tasks.ui.pages.editor.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.app.tasks.R
import com.app.tasks.ui.components.ChipItem
import com.app.tasks.ui.components.FilterChipGroup

@Composable
fun TasksCategoryChip(
    modifier: Modifier = Modifier,
    items: List<ChipItem>,
    defaultSelectedItemIndex: Int,
    isLoading: Boolean = false,
    onCategorySelected: (Int) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (isLoading) {
            Text(
                text = stringResource(R.string.tip_no_category_chip),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        FilterChipGroup(
            modifier = Modifier,
            items = items,
            defaultSelectedItemIndex = defaultSelectedItemIndex,
            onSelectedChanged = onCategorySelected
        )
    }
}