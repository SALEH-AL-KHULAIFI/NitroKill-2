package com.isx3i.nitrokill

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.isx3i.nitrokill.data.PrefsManager
import com.isx3i.nitrokill.service.SpeedMonitorService
import com.isx3i.nitrokill.ui.MainScreen
import com.isx3i.nitrokill.ui.NitroKillTheme
import com.isx3i.nitrokill.ui.OptionsScreen
import com.isx3i.nitrokill.util.LocaleHelper
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var prefs: PrefsManager

    private val notificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* if denied, the ongoing notification simply won't show — service still runs */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PrefsManager(this)
        requestNotificationPermissionIfNeeded()

        setContent {
            var screen by remember { mutableStateOf(Screen.MAIN) }

            NitroKillTheme {
                when (screen) {
                    Screen.MAIN -> MainScreen(
                        onOpenOptions = { screen = Screen.OPTIONS },
                        onReenable = { SpeedMonitorService.start(this) },
                        onHideAndExit = { moveTaskToBack(true) },
                        onToggleMonitor = { enabled -> toggleMonitoring(enabled) }
                    )
                    Screen.OPTIONS -> OptionsScreen(
                        prefs = prefs,
                        onBack = { screen = Screen.MAIN },
                        onLanguageChanged = { lang -> applyLanguageAndRestart(lang) },
                        onOpenNotificationSettings = { openNotificationSettings() }
                    )
                }
            }
        }
    }

    private enum class Screen { MAIN, OPTIONS }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun toggleMonitoring(enabled: Boolean) {
        lifecycleScope.launch { prefs.setMonitorEnabled(enabled) }
        if (enabled) SpeedMonitorService.start(this) else SpeedMonitorService.stop(this)
    }

    private fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    }

    private fun applyLanguageAndRestart(lang: String) {
        lifecycleScope.launch {
            prefs.setLanguage(lang)
            LocaleHelper.applyToResources(this@MainActivity, lang)
            recreate()
        }
    }
}
