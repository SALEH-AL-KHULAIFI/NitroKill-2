package com.isx3i.nitrokill.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

/**
 * Handles NitroKill's in-app language selection.
 *
 * Language is intentionally read from SharedPreferences during application
 * startup instead of DataStore, because attachBaseContext() must remain
 * synchronous and lightweight.
 */
object LocaleHelper {

    private const val LANGUAGE_PREFS = "nitrokill_language"
    private const val KEY_LANGUAGE = "language"

    private const val DEFAULT_LANGUAGE = "ar"

    /**
     * Reads the saved language synchronously.
     *
     * This method is safe to call from Application.attachBaseContext()
     * because SharedPreferences does not require coroutines or DataStore.
     */
    fun readSavedLanguageBlocking(context: Context): String {
        return try {
            context
                .getSharedPreferences(
                    LANGUAGE_PREFS,
                    Context.MODE_PRIVATE
                )
                .getString(
                    KEY_LANGUAGE,
                    DEFAULT_LANGUAGE
                )
                ?.takeIf { it == "ar" || it == "en" }
                ?: DEFAULT_LANGUAGE
        } catch (_: Exception) {
            DEFAULT_LANGUAGE
        }
    }

    /**
     * Saves the selected language.
     *
     * SharedPreferences is used here so the same value is immediately
     * available during the next application startup.
     */
    fun saveLanguage(context: Context, languageCode: String) {
        val safeLanguage =
            if (languageCode == "en") "en" else "ar"

        context
            .getSharedPreferences(
                LANGUAGE_PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(KEY_LANGUAGE, safeLanguage)
            .apply()
    }

    /**
     * Wraps the context with the selected locale.
     */
    fun wrap(
        context: Context,
        languageCode: String
    ): Context {

        val safeLanguage =
            if (languageCode == "en") "en" else "ar"

        val locale = Locale(safeLanguage)

        Locale.setDefault(locale)

        val config = Configuration(
            context.resources.configuration
        )

        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    /**
     * Applies the selected language to an existing context.
     *
     * Used after changing language from the settings screen.
     */
    fun applyToResources(
        context: Context,
        languageCode: String
    ) {
        val safeLanguage =
            if (languageCode == "en") "en" else "ar"

        saveLanguage(context, safeLanguage)

        val locale = Locale(safeLanguage)

        Locale.setDefault(locale)

        val resources = context.resources

        val config = Configuration(
            resources.configuration
        )

        config.setLocale(locale)
        config.setLayoutDirection(locale)

        /*
         * updateConfiguration() is deprecated on newer Android versions,
         * but is retained for old Android compatibility.
         */
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            resources.updateConfiguration(
                config,
                resources.displayMetrics
            )
        }
    }
}
