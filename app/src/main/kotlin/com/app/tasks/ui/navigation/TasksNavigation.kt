package com.app.tasks.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.tasks.ui.pages.editor.TasksEditorPage
import com.app.tasks.ui.pages.main.MainPage
import com.app.tasks.ui.pages.settings.SettingsAbout
import com.app.tasks.ui.pages.settings.SettingsAboutLicence
import com.app.tasks.ui.pages.settings.SettingsAppearance
import com.app.tasks.ui.pages.settings.SettingsData
import com.app.tasks.ui.pages.settings.SettingsDataCategory
import com.app.tasks.ui.pages.settings.SettingsDeveloperOptions
import com.app.tasks.ui.pages.settings.SettingsDeveloperOptionsPadding
import com.app.tasks.ui.pages.settings.SettingsInterface
import com.app.tasks.ui.pages.settings.SettingsMain
import com.app.tasks.ui.theme.materialSharedAxisXIn
import com.app.tasks.ui.theme.materialSharedAxisXOut
import com.app.tasks.ui.viewmodels.MainViewModel

private const val INITIAL_OFFSET_FACTOR = 0.10f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TasksNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TasksScreen.Main.name,
    viewModel: MainViewModel
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = {
                materialSharedAxisXIn(
                    initialOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() }
                )
            },
            exitTransition = {
                materialSharedAxisXOut(
                    targetOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() }
                )
            },
            popEnterTransition = {
                materialSharedAxisXIn(
                    initialOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() }
                )
            },
            popExitTransition = {
                materialSharedAxisXOut(
                    targetOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() }
                )
            },
            modifier = modifier
        ) {
            composable(TasksScreen.Main.name) {
                MainPage(
                    viewModel = viewModel,
                    toTodoEditPage = { navController.navigate(TasksScreen.TasksEditor.name) },
                    toSettingsPage = { navController.navigate(TasksScreen.SettingsMain.name) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(TasksScreen.TasksEditor.name) {
                TasksEditorPage(
                    toDo = viewModel.selectedEditTodo,
                    onSave = {
                        viewModel.addTodo(it)
                        if (viewModel.selectedEditTodo?.isCompleted != true && it.isCompleted) {
                            viewModel.playConfetti()
                        }
                        navController.navigateUp()
                    },
                    onDelete = {
                        if (viewModel.selectedEditTodo !== null) {
                            viewModel.deleteTodo(viewModel.selectedEditTodo!!)
                            viewModel.setEditTodoItem(null)
                        }
                        navController.navigateUp()
                    },
                    onNavigateUp = { navController.navigateUp() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(TasksScreen.SettingsMain.name) {
                SettingsMain(
                    toAppearancePage = { navController.navigate(TasksScreen.SettingsAppearance.name) },
                    toAboutPage = { navController.navigate(TasksScreen.SettingsAbout.name) },
                    toInterfacePage = { navController.navigate(TasksScreen.SettingsInterface.name) },
                    toDataPage = { navController.navigate(TasksScreen.SettingsData.name) },
                    onNavigateUp = { navController.navigateUp() },
                )
            }

            composable(TasksScreen.SettingsAppearance.name) {
                SettingsAppearance(onNavigateUp = { navController.navigateUp() })
            }

            composable(TasksScreen.SettingsInterface.name) {
                SettingsInterface(onNavigateUp = { navController.navigateUp() })
            }

            composable(TasksScreen.SettingsData.name) {
                SettingsData(
                    viewModel = viewModel,
                    toCategoryManager = { navController.navigate(TasksScreen.SettingsDataCategory.name) },
                    onNavigateUp = { navController.navigateUp() }
                )
            }

            composable(TasksScreen.SettingsDataCategory.name) {
                SettingsDataCategory(onNavigateUp = { navController.navigateUp() })
            }

            composable(TasksScreen.SettingsAbout.name) {
                SettingsAbout(
                    toLicencePage = { navController.navigate(TasksScreen.SettingsAboutLicence.name) },
                    toDevPage = { navController.navigate(TasksScreen.SettingsDev.name) },
                    onNavigateUp = { navController.navigateUp() },
                )
            }

            composable(TasksScreen.SettingsDev.name) {
                SettingsDeveloperOptions(
                    toPaddingPage = { navController.navigate(TasksScreen.SettingsDevPadding.name) },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable(TasksScreen.SettingsDevPadding.name) {
                SettingsDeveloperOptionsPadding(onNavigateUp = { navController.navigateUp() })
            }
        }
    }
}