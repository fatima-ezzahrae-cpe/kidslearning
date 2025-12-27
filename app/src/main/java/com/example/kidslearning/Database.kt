package com.example.kidslearning

import android.content.Context
import androidx.room.*
import androidx.lifecycle.LiveData

// Entity for storing letter progress
@Entity(tableName = "letter_progress")
data class LetterProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "letter")
    val letter: String,

    @ColumnInfo(name = "language")
    val language: String, // "FR" or "AR"

    @ColumnInfo(name = "times_practiced")
    val timesPracticed: Int = 0,

    @ColumnInfo(name = "last_practiced")
    val lastPracticed: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_mastered")
    val isMastered: Boolean = false
)

// DAO for database operations
@Dao
interface LetterProgressDao {

    @Query("SELECT * FROM letter_progress WHERE language = :language ORDER BY letter")
    fun getAllProgress(language: String): LiveData<List<LetterProgress>>

    @Query("SELECT * FROM letter_progress WHERE letter = :letter AND language = :language")
    suspend fun getProgress(letter: String, language: String): LetterProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: LetterProgress)

    @Update
    suspend fun updateProgress(progress: LetterProgress)

    @Query("UPDATE letter_progress SET times_practiced = times_practiced + 1, last_practiced = :timestamp WHERE letter = :letter AND language = :language")
    suspend fun incrementPracticeCount(letter: String, language: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE letter_progress SET is_mastered = :mastered WHERE letter = :letter AND language = :language")
    suspend fun setMastered(letter: String, language: String, mastered: Boolean)

    @Query("DELETE FROM letter_progress WHERE language = :language")
    suspend fun deleteAllProgress(language: String)
}

// Database class
@Database(entities = [LetterProgress::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun letterProgressDao(): LetterProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kids_learning_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Repository for managing data operations
class LetterProgressRepository(private val letterProgressDao: LetterProgressDao) {

    fun getAllProgress(language: String): LiveData<List<LetterProgress>> {
        return letterProgressDao.getAllProgress(language)
    }

    suspend fun getProgress(letter: String, language: String): LetterProgress? {
        return letterProgressDao.getProgress(letter, language)
    }

    suspend fun insertProgress(progress: LetterProgress) {
        letterProgressDao.insertProgress(progress)
    }

    suspend fun updateProgress(progress: LetterProgress) {
        letterProgressDao.updateProgress(progress)
    }

    suspend fun incrementPracticeCount(letter: String, language: String) {
        val existing = letterProgressDao.getProgress(letter, language)
        if (existing == null) {
            // Create new progress entry
            letterProgressDao.insertProgress(
                LetterProgress(
                    letter = letter,
                    language = language,
                    timesPracticed = 1
                )
            )
        } else {
            letterProgressDao.incrementPracticeCount(letter, language)
        }
    }

    suspend fun setMastered(letter: String, language: String, mastered: Boolean) {
        letterProgressDao.setMastered(letter, language, mastered)
    }
}