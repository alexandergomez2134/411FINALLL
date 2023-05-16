package com.example.quizlingo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

const val INITIAL_SCORE_VALUE = 0
private const val TAG = "MainActivityViewModel"

class MainActivityViewModel : ViewModel() {
    private var sourceLanguageCode = "en"
    private var sourceLanguageTitle = "English"
    private var targetLanguageCode = "es"
    private var targetLanguageTitle = "Spanish"

    private var correct1 = INITIAL_SCORE_VALUE
    private var correct2 = INITIAL_SCORE_VALUE
    private var correct3 = INITIAL_SCORE_VALUE
    private var correct4 = INITIAL_SCORE_VALUE

    // Create instance to access AppPreferencesRepository
    private val prefs = AppPreferencesRepository.getRepository()

    // Save number of corrects
    fun saveCorrect1() {
        viewModelScope.launch {
            prefs.saveCorrect1(correct1)
            Log.d(TAG, "Saving correct1: $correct1")
        }
    }

    fun saveCorrect2() {
        viewModelScope.launch {
            prefs.saveCorrect2(correct2)
            Log.d(TAG, "Saving correct2: $correct2")
        }
    }

    fun saveCorrect3() {
        viewModelScope.launch {
            prefs.saveCorrect3(correct3)
            Log.d(TAG, "Saving correct3: $correct3")
        }
    }

    fun saveCorrect4() {
        viewModelScope.launch {
            prefs.saveCorrect4(correct4)
            Log.d(TAG, "Saving correct4: $correct4")
        }
    }


    // Load number of corrects
    fun loadCorrect1() {
        GlobalScope.launch {
            prefs.correct1.collectLatest {
                correct1 = it
                Log.d(TAG, "Loaded correct1: $correct1")
            }
        }
        sleep(100)
    }

    fun loadCorrect2() {
        GlobalScope.launch {
            prefs.correct2.collectLatest {
                correct2 = it
                Log.d(TAG, "Loaded correct2: $correct2")
            }
        }
        sleep(100)
    }

    fun loadCorrect3() {
        GlobalScope.launch {
            prefs.correct3.collectLatest {
                correct3 = it
                Log.d(TAG, "Loaded correct3: $correct3")
            }
        }
        sleep(100)
    }

    fun loadCorrect4() {
        GlobalScope.launch {
            prefs.correct4.collectLatest {
                correct4 = it
                Log.d(TAG, "Loaded correct4: $correct4")
            }
        }
        sleep(100)
    }


    fun setCorrect1(value: Int) {
        this.correct1 = value
        Log.d(TAG, "Question 1 Point: ${this.correct1}")
        saveCorrect1()
    }

    fun setCorrect2(value: Int) {
        this.correct2 = value
        Log.d(TAG, "Question 2 Point: ${this.correct2}")
        saveCorrect2()
    }

    fun setCorrect3(value: Int) {
        this.correct3 = value
        Log.d(TAG, "Question 3 Point: ${this.correct3}")
        saveCorrect3()

    }

    fun setCorrect4(value: Int) {
        this.correct4 = value
        Log.d(TAG, "Question 4 Point: ${this.correct4}")
        saveCorrect4()
    }


    fun getCorrect1(): Int {
        return this.correct1
    }

    fun getCorrect2(): Int {
        return this.correct2
    }

    fun getCorrect3(): Int {
        return this.correct3
    }

    fun getCorrect4(): Int {
        return this.correct4
    }


    fun saveTargetLanguageCode() {
        viewModelScope.launch {
            prefs.saveTargetLanguageCode(targetLanguageCode)
            Log.d(TAG, "Saving targetLanguageCode: $targetLanguageCode")
        }
    }

    fun saveTargetLanguageTitle() {
        viewModelScope.launch {
            prefs.saveTargetLanguageTitle(targetLanguageTitle)
            Log.d(TAG, "Saving targetLanguageTitle: $targetLanguageTitle")
        }
    }


    fun loadTargetLanguageCode() {
        GlobalScope.launch {
            prefs.targetLanguageCode.collectLatest {
                targetLanguageCode = it
                Log.d(TAG, "Loaded targetLanguageCode: $targetLanguageCode")
            }
        }
        sleep(100)
    }

    fun loadTargetLanguageTitle() {
        GlobalScope.launch {
            prefs.targetLanguageTitle.collectLatest {
                targetLanguageTitle = it
                Log.d(TAG, "Loaded targetLanguageTitle: $targetLanguageTitle")
            }
        }
        sleep(100)
    }


    fun getSourceLanguageCode(): String {
        return this.sourceLanguageCode
        Log.d(TAG, "ViewModel sourceLanguageCode: ${this.sourceLanguageCode}")
    }

    fun getTargetLanguageCode(): String {
        return this.targetLanguageCode
        Log.d(TAG, "ViewModel targetLanguageCode: ${this.targetLanguageCode}")
    }

    fun getTargetLanguageTitle(): String {
        return this.targetLanguageTitle
        Log.d(TAG, "ViewModel targetLanguageTitle: ${this.targetLanguageTitle}")
    }


    fun setTargetLanguageCode(value: String) {
        this.targetLanguageCode = value
        saveTargetLanguageCode()
    }

    fun setTargetLanguageTitle(value: String) {
        this.targetLanguageTitle = value
        saveTargetLanguageTitle()
    }
}