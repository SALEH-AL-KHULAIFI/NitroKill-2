package com.isx3i.nitrokill

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import com.isx3i.nitrokill.util.LocaleHelper

class NitroKillApp : Application() {

    companion object {
        const val SPEED_CHANNEL_ID = "nitrokill_speed_channel"
    }

    override fun attachBaseContext(base: Context) {
        /*
         * IMPORTANT:
         * Do not access DataStore or use runBlocking here.
         *
         * attachBaseContext() runs very early during application startup.
         * Reading the language from SharedPreferences keeps startup
         * synchronous, lightweight, and safe.
         */
        val savedLang = LocaleHelper.readSavedLanguageBlocking(base)

        super.attachBaseContext(
            LocaleHelper.wrap(base, savedLang)
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            SPEED_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.notification_channel_desc)
            setShowBadge(false)
        }

        manager.createNotificationChannel(channel)
    }
}
