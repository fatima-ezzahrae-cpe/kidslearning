package com.example.kidslearning

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.kidslearning.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences

    companion object {
        const val PREFS_NAME = "KidsLearningPrefs"
        const val PREF_SOUND_ENABLED = "sound_enabled"
        const val PREF_SOUND_VOLUME = "sound_volume"
        const val PREF_VIBRATION_ENABLED = "vibration_enabled"
        const val PREF_DARK_MODE = "dark_mode"
        const val PREF_AUTOPLAY_SOUND = "autoplay_sound"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setupUI()
        loadSettings()
    }

    private fun setupUI() {
        binding.apply {
            // Back button
            btnBack.setOnClickListener { finish() }

            // Sound toggle
            switchSound.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean(PREF_SOUND_ENABLED, isChecked).apply()
                seekBarVolume.isEnabled = isChecked
            }

            // Volume control
            seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    tvVolumeValue.text = "$progress%"
                    if (fromUser) {
                        prefs.edit().putInt(PREF_SOUND_VOLUME, progress).apply()
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // Vibration toggle
            switchVibration.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean(PREF_VIBRATION_ENABLED, isChecked).apply()
            }

            // Dark mode toggle
            switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean(PREF_DARK_MODE, isChecked).apply()
                applyDarkMode(isChecked)
            }

            // Autoplay sound toggle
            switchAutoplay.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean(PREF_AUTOPLAY_SOUND, isChecked).apply()
            }

            // Reset progress button
            btnResetProgress.setOnClickListener {
                showResetConfirmationDialog()
            }

            // About button
            btnAbout.setOnClickListener {
                showAboutDialog()
            }
        }
    }

    private fun loadSettings() {
        binding.apply {
            // Load saved settings
            val soundEnabled = prefs.getBoolean(PREF_SOUND_ENABLED, true)
            val volume = prefs.getInt(PREF_SOUND_VOLUME, 80)
            val vibrationEnabled = prefs.getBoolean(PREF_VIBRATION_ENABLED, true)
            val darkMode = prefs.getBoolean(PREF_DARK_MODE, false)
            val autoplay = prefs.getBoolean(PREF_AUTOPLAY_SOUND, true)

            switchSound.isChecked = soundEnabled
            seekBarVolume.progress = volume
            seekBarVolume.isEnabled = soundEnabled
            tvVolumeValue.text = "$volume%"
            switchVibration.isChecked = vibrationEnabled
            switchDarkMode.isChecked = darkMode
            switchAutoplay.isChecked = autoplay
        }
    }

    private fun applyDarkMode(enabled: Boolean) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showResetConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Réinitialiser la progression")
            .setMessage("Êtes-vous sûr de vouloir effacer toute la progression? Cette action est irréversible.")
            .setPositiveButton("Oui") { _, _ ->
                resetProgress()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun resetProgress() {
        // Clear database
        val database = AppDatabase.getDatabase(applicationContext)
        Thread {
            database.clearAllTables()
            runOnUiThread {
                android.widget.Toast.makeText(
                    this,
                    "Progression réinitialisée",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }

    private fun showAboutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("À propos")
            .setMessage("""
                Kids Learning v1.0
                
                Une application éducative pour apprendre les alphabets arabe et français.
                
                Développé avec ❤️ pour l'éducation des enfants.
                
                © 2024 Kids Learning
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
}