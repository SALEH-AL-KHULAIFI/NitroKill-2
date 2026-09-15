package com.isx3i.nitrokill.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.isx3i.nitrokill.util.LocaleHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "nitrokill_prefs"
)

/**
 * Stores NitroKill settings.
 *
 * DataStore is used for normal application settings.
 *
 * IMPORTANT:
 * The language is also mirrored into SharedPreferences through
 * LocaleHelper so it can safely be read during Application.attachBaseContext().
 */
class PrefsManager(
    private val context: Context
) {

    companion object {

        val KEY_LANGUAGE =
            stringPreferencesKey("language")

        val KEY_MONITOR_ENABLED =
            booleanPreferencesKey("monitor_enabled")
    }

    /**
     * Current application language.
     *
     * DataStore remains the source used by the UI after the application
     * has started normally.
     */
    val language: Flow<String> =
        context.dataStore.data.map {
            it[KEY_LANGUAGE] ?: "ar"
        }

    /**
     * Whether the speed monitor is enabled.
     */
    val monitorEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[KEY_MONITOR_ENABLED] ?: false
        }

    /**
     * Save language.
     *
     * We write to both:
     *
     * 1. SharedPreferences -> safe early startup access.
     * 2. DataStore -> normal application settings/UI.
     */
    suspend fun setLanguage(lang: String) {

        val safeLanguage =
            if (lang == "en") "en" else "ar"

        /*
         * Make the language available immediately for the next launch.
         */
        LocaleHelper.saveLanguage(
            context,
            safeLanguage
        )

        /*
         * Keep DataStore synchronized with the language.
         */
        context.dataStore.edit {
            it[KEY_LANGUAGE] = safeLanguage
        }
    }

    /**
     * Enable or disable the monitoring service.
     */
    suspend fun setMonitorEnabled(
        enabled: Boolean
    ) {
        context.dataStore.edit {
            it[KEY_MONITOR_ENABLED] = enabled
        }
    }
}
