package com.app.tasks.ui.pages.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.app.tasks.R
import com.app.tasks.constants.Constants
import com.app.tasks.logic.datastore.DataStoreManager
import com.app.tasks.logic.model.SortingMethod
import com.app.tasks.ui.components.LargeTopAppBarScaffold
import com.app.tasks.ui.pages.settings.components.SettingsCategory
import com.app.tasks.ui.pages.settings.components.SettingsItem
import com.app.tasks.ui.pages.settings.components.SettingsPlainBox
import com.app.tasks.ui.pages.settings.components.SettingsRadioDialog
import com.app.tasks.ui.pages.settings.components.SettingsRadioOptions
import com.app.tasks.ui.pages.settings.components.SwitchSettingsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsInterface(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showCompleted by DataStoreManager.showCompletedFlow.collectAsState(initial = Constants.PREF_SHOW_COMPLETED_DEFAULT)
    val secureMode by DataStoreManager.secureModeFlow.collectAsState(initial = Constants.PREF_SECURE_MODE_DEFAULT)
    val sortingMethod by DataStoreManager.sortingMethodFlow.collectAsState(initial = Constants.PREF_SORTING_METHOD_DEFAULT)
    val hapticFeedback by DataStoreManager.hapticFeedbackFlow.collectAsState(initial = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showSortingMethodDialog by rememberSaveable { mutableStateOf(false) }
    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_interface_interaction),
        onBack = onNavigateUp,
        scrollBehavior = scrollBehavior,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                SettingsCategory(stringResource(R.string.pref_category_tasks_list))
            }
            item {
                SwitchSettingsItem(
                    checked = showCompleted,
                    leadingIcon = Icons.Outlined.Checklist,
                    title = stringResource(R.string.pref_show_completed),
                    description = stringResource(R.string.pref_show_completed_desc),
                    onCheckedChange = { scope.launch { DataStoreManager.setShowCompleted(it) } },
                )
            }
            item {
                SettingsItem(
                    leadingIcon = Icons.AutoMirrored.Outlined.Sort,
                    title = stringResource(R.string.pref_sorting_method),
                    description = SortingMethod.fromId(sortingMethod).getDisplayName(context),
                    onClick = { showSortingMethodDialog = true }
                )
            }

            item {
                SettingsCategory(stringResource(R.string.pref_category_global))
            }
            item {
                SwitchSettingsItem(
                    checked = secureMode,
                    leadingIcon = Icons.Outlined.Shield,
                    title = stringResource(R.string.pref_secure_mode),
                    description = stringResource(R.string.pref_secure_mode_desc),
                    onCheckedChange = { scope.launch { DataStoreManager.setSecureMode(it) } }
                )
            }
            item {
                SwitchSettingsItem(
                    checked = hapticFeedback,
                    leadingIcon = Icons.Outlined.Vibration,
                    title = stringResource(R.string.pref_haptic_feedback),
                    description = stringResource(R.string.pref_haptic_feedback_desc),
                    onCheckedChange = { scope.launch { DataStoreManager.setHapticFeedback(it) } }
                )
                SettingsPlainBox(stringResource(R.string.pref_haptic_feedback_more_info))
            }
        }
    }

    val sortingList = remember {
        SortingMethod.entries.map {
            SettingsRadioOptions(
                id = it.id,
                text = it.getDisplayName(context)
            )
        }
    }
    SettingsRadioDialog(
        visible = showSortingMethodDialog,
        title = stringResource(R.string.pref_sorting_method),
        currentOptions = SettingsRadioOptions(
            id = sortingMethod,
            text = SortingMethod.fromId(sortingMethod).getDisplayName(context)
        ),
        options = sortingList,
        onSelect = { scope.launch { DataStoreManager.setSortingMethod(it) } },
        onDismiss = { showSortingMethodDialog = false }
    )
}