package com.example.kidslearning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.kidslearning.databinding.ActivityPracticeBinding

class PracticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPracticeBinding
    private lateinit var letter: Letter
    private lateinit var alphabetType: AlphabetType

    companion object {
        private const val EXTRA_LETTER = "letter"
        private const val EXTRA_ALPHABET_TYPE = "alphabet_type"

        fun newIntent(context: Context, letter: Letter, type: AlphabetType): Intent {
            return Intent(context, PracticeActivity::class.java).apply {
                putExtra(EXTRA_LETTER, letter.character)
                putExtra(EXTRA_ALPHABET_TYPE, type.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val letterChar = intent.getStringExtra(EXTRA_LETTER) ?: "A"
        alphabetType = AlphabetType.valueOf(
            intent.getStringExtra(EXTRA_ALPHABET_TYPE) ?: AlphabetType.FRENCH.name
        )

        letter = Letter(letterChar, "", "", R.drawable.alphabet)


        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            tvTitle.text = "Pratique d'écriture"
            tvLetterCard.text = letter.character

            btnBack.setOnClickListener { finish() }

            btnClear.setOnClickListener {
                drawingView.clearCanvas()
                tvValidation.visibility = View.GONE
            }

            btnValidate.setOnClickListener {
                validateDrawing()
            }

            // Set the letter to trace
            drawingView.setLetterToTrace(letter.character)
        }
    }

    private fun validateDrawing() {
        // Simple validation - show message
        binding.tvValidation.apply {
            visibility = View.VISIBLE
            text = "Bravo! 🎉"
        }
    }
}
