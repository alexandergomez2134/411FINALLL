package com.example.quizlingo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

// CPSC 411, Section 03, Final Project: Quizlingo

class AppPreferencesRepository private constructor(private val dataStore: DataStore<Preferences>){

    private val CORRECT1 = intPreferencesKey("correct1")
    private val CORRECT2 = intPreferencesKey("correct2")
    private val CORRECT3 = intPreferencesKey("correct3")
    private val CORRECT4 = intPreferencesKey("correct4")

    private val TARGET_LANGUAGECODE = stringPreferencesKey("targetLanguageCode")
    private val TARGET_LANGUAGETITLE = stringPreferencesKey("targetLanguageTitle")


    // Reads the value associated with keys, retrieve
    val correct1: kotlinx.coroutines.flow.Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[CORRECT1] ?: INITIAL_SCORE_VALUE
    }.distinctUntilChanged()

    val correct2: kotlinx.coroutines.flow.Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[CORRECT2] ?: INITIAL_SCORE_VALUE
    }.distinctUntilChanged()

    val correct3: kotlinx.coroutines.flow.Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[CORRECT3] ?: INITIAL_SCORE_VALUE
    }.distinctUntilChanged()

    val correct4: kotlinx.coroutines.flow.Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[CORRECT4] ?: INITIAL_SCORE_VALUE
    }.distinctUntilChanged()


    val targetLanguageCode: kotlinx.coroutines.flow.Flow<String> =
        this.dataStore.data.map { prefs ->
            prefs[TARGET_LANGUAGECODE] ?: "es"
        }.distinctUntilChanged()

    val targetLanguageTitle: kotlinx.coroutines.flow.Flow<String> =
        this.dataStore.data.map { prefs ->
            prefs[TARGET_LANGUAGETITLE] ?: "Spanish"
        }.distinctUntilChanged()


    // Saves in the DataStore
    private suspend fun saveIntValue(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    private suspend fun saveStringValue(key: Preferences.Key<String>, value: String) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }


    // Saving Number of Correct...
    suspend fun saveCorrect1(value: Int) {
        saveIntValue(CORRECT1, value)
    }
    suspend fun saveCorrect2(value: Int) {
        saveIntValue(CORRECT2, value)
    }
    suspend fun saveCorrect3(value: Int) {
        saveIntValue(CORRECT3, value)
    }
    suspend fun saveCorrect4(value: Int) {
        saveIntValue(CORRECT4, value)
    }


    suspend fun saveTargetLanguageCode(value: String) {
        saveStringValue(TARGET_LANGUAGECODE, value)
    }
    suspend fun saveTargetLanguageTitle(value: String) {
        saveStringValue(TARGET_LANGUAGETITLE, value)
    }

    // Provides access to static members and methods.
    companion object {
        private const val PREFERENCES_DATA_FILE_NAME = "settings"
        private var INSTANCE_OF_APP_REPOSITORY: AppPreferencesRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE_OF_APP_REPOSITORY == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(PREFERENCES_DATA_FILE_NAME)
                }
                INSTANCE_OF_APP_REPOSITORY = AppPreferencesRepository(dataStore)
            }
        }

        fun getRepository(): AppPreferencesRepository {
            return INSTANCE_OF_APP_REPOSITORY
                ?: throw IllegalStateException("AppPreferencesRepository not initialized yet")
        }
    }
}