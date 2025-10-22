package com.app.tasks.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.tasks.constants.Constants
import com.app.tasks.logic.datastore.DataStoreManager
import com.app.tasks.ui.components.Konfetti
import com.app.tasks.ui.navigation.TasksNavigation
import com.app.tasks.ui.theme.DarkMode
import com.app.tasks.ui.theme.PaletteStyle
import com.app.tasks.ui.theme.TasksTheme
import com.app.tasks.ui.viewmodels.MainViewModel
import com.app.tasks.utils.VibrationUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val showConfetti = mainViewModel.showConfetti
            val dynamicColor by DataStoreManager.dynamicColorFlow.collectAsState(initial = Constants.PREF_DYNAMIC_COLOR_DEFAULT)
            val paletteStyle by DataStoreManager.paletteStyleFlow.collectAsState(initial = Constants.PREF_PALETTE_STYLE_DEFAULT)
            val contrastLevel by DataStoreManager.contrastLevelFlow.collectAsState(initial = Constants.PREF_CONTRAST_LEVEL_DEFAULT)
            val darkMode by DataStoreManager.darkModeFlow.collectAsState(initial = Constants.PREF_DARK_MODE_DEFAULT)
            val secureMode by DataStoreManager.secureModeFlow.collectAsState(initial = Constants.PREF_SECURE_MODE_DEFAULT)
            val hapticFeedback by DataStoreManager.hapticFeedbackFlow.collectAsState(initial = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT)

            val darkTheme = when (DarkMode.fromId(darkMode)) {
                DarkMode.FollowSystem -> isSystemInDarkTheme()
                DarkMode.Light -> false
                DarkMode.Dark -> true
            }

            LaunchedEffect(darkMode) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            LaunchedEffect(secureMode) {
                if (secureMode) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }

            LaunchedEffect(hapticFeedback) {
                VibrationUtils.setEnabled(hapticFeedback)
            }

            TasksTheme(
                darkTheme = darkTheme,
                style = PaletteStyle.fromId(paletteStyle),
                contrastLevel = contrastLevel.toDouble(),
                dynamicColor = dynamicColor
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TasksNavigation(
                        viewModel = mainViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                    Konfetti(state = showConfetti)
                }
            }
        }
    }
}