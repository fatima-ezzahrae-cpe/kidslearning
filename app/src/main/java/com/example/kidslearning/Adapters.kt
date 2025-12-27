package com.example.kidslearning

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kidslearning.databinding.ItemLanguageBinding
import com.example.kidslearning.databinding.ItemLetterGridBinding

// Language Adapter for Home Screen
class LanguageAdapter(
    private val languages: List<Language>,
    private val onLanguageClick: (Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(language: Language) {
            binding.apply {
                tvLanguageName.text = language.name
                tvLanguagePreview.text = language.preview
                tvLanguageCode.text = language.code

                // Set background color
                try {
                    cardLanguage.setCardBackgroundColor(Color.parseColor(language.color))
                } catch (e: Exception) {
                    cardLanguage.setCardBackgroundColor(Color.BLUE)
                }

                root.setOnClickListener {
                    onLanguageClick(language)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount() = languages.size
}

// Letter Grid Adapter for "All Letters" Dialog
class LetterGridAdapter(
    private val letters: List<Letter>,
    private val onLetterClick: (Letter) -> Unit
) : RecyclerView.Adapter<LetterGridAdapter.LetterViewHolder>() {

    private var selectedPosition = 0

    inner class LetterViewHolder(private val binding: ItemLetterGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(letter: Letter, position: Int) {
            binding.apply {
                tvLetter.text = letter.character

                // Highlight selected letter
                if (position == selectedPosition) {
                    cardLetter.setCardBackgroundColor(
                        root.context.getColor(R.color.purple_500)
                    )
                    tvLetter.setTextColor(Color.WHITE)
                } else {
                    cardLetter.setCardBackgroundColor(Color.WHITE)
                    tvLetter.setTextColor(Color.BLACK)
                }

                root.setOnClickListener {
                    val oldPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(oldPosition)
                    notifyItemChanged(selectedPosition)
                    onLetterClick(letter)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val binding = ItemLetterGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LetterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.bind(letters[position], position)
    }

    override fun getItemCount() = letters.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }
}