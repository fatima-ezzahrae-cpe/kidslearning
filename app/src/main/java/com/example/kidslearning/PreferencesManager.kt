package com.example.kidslearning

import android.content.Context
import android.content.SharedPreferences
import com.example.kidslearning.SettingsActivity

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        SettingsActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(SettingsActivity.PREF_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(SettingsActivity.PREF_SOUND_ENABLED, value).apply()

    var soundVolume: Int
        get() = prefs.getInt(SettingsActivity.PREF_SOUND_VOLUME, 80)
        set(value) = prefs.edit().putInt(SettingsActivity.PREF_SOUND_VOLUME, value).apply()

    var isVibrationEnabled: Boolean
        get() = prefs.getBoolean(SettingsActivity.PREF_VIBRATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(SettingsActivity.PREF_VIBRATION_ENABLED, value).apply()

    var isDarkMode: Boolean
        get() = prefs.getBoolean(SettingsActivity.PREF_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(SettingsActivity.PREF_DARK_MODE, value).apply()

    var isAutoplaySoundEnabled: Boolean
        get() = prefs.getBoolean(SettingsActivity.PREF_AUTOPLAY_SOUND, true)
        set(value) = prefs.edit().putBoolean(SettingsActivity.PREF_AUTOPLAY_SOUND, value).apply()

    companion object {
        @Volatile
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}
