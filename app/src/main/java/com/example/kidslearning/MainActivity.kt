package com.example.kidslearning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kidslearning.databinding.ActivityMainBinding
import android.content.Intent


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguageOptions()
    }

    private fun setupLanguageOptions() {
        val languages = listOf(
            Language("Français", "A, B, C...", "FR", "#2196F3"),
            Language("العربية", "أ، ب، ت...", "AR", "#4CAF50")
        )

        binding.rvLanguages.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = LanguageAdapter(languages) { language ->
                navigateToAlphabet(language)
            }
        }

        // Setup settings button
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun navigateToAlphabet(language: Language) {
        val intent = if (language.code == "FR") {
            AlphabetActivity.newIntent(this, AlphabetType.FRENCH)
        } else {
            AlphabetActivity.newIntent(this, AlphabetType.ARABIC)
        }
        startActivity(intent)
    }
}

data class Language(
    val name: String,
    val preview: String,
    val code: String,
    val color: String
)