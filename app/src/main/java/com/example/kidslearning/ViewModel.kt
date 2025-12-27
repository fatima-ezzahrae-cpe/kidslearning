package com.example.kidslearning

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LetterProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LetterProgressRepository

    init {
        val letterProgressDao = AppDatabase.getDatabase(application).letterProgressDao()
        repository = LetterProgressRepository(letterProgressDao)
    }

    fun getAllProgress(language: String): LiveData<List<LetterProgress>> {
        return repository.getAllProgress(language)
    }

    fun incrementPracticeCount(letter: String, language: String) {
        viewModelScope.launch {
            repository.incrementPracticeCount(letter, language)
        }
    }

    fun setMastered(letter: String, language: String, mastered: Boolean) {
        viewModelScope.launch {
            repository.setMastered(letter, language, mastered)
        }
    }

    fun insertProgress(progress: LetterProgress) {
        viewModelScope.launch {
            repository.insertProgress(progress)
        }
    }
}