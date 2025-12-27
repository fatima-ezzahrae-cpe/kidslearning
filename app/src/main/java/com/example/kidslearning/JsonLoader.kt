package com.example.kidslearning.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

// Data classes for JSON
data class AlphabetData(
    val letters: List<LetterData>
)

data class LetterData(
    val character: String,
    val example: String,
    val exampleTranslation: String,
    val fact: String,
    val soundFile: String
)

// JSON Loader utility
object JsonLoader {

    fun loadAlphabetData(context: Context, language: String): AlphabetData? {
        return try {
            val fileName = if (language == "FR") {
                "french_alphabet.json"
            } else {
                "arabic_alphabet.json"
            }

            val jsonString = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }

            val gson = Gson()
            gson.fromJson(jsonString, AlphabetData::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
