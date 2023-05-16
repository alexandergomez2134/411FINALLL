package com.example.quizlingo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizlingo.api2.ModelLanguage
import com.google.android.material.button.MaterialButton
import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.*
import kotlin.collections.ArrayList
import java.util.Locale

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var languageArrayList: ArrayList<ModelLanguage>? = null

    private lateinit var targetLanguageChooseBtn: MaterialButton
    private lateinit var startButton: Button

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        AppPreferencesRepository.initialize(this)
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startQuiz)
        targetLanguageChooseBtn = findViewById(R.id.targetLanguageChooseBtn)

        mainActivityViewModel.loadTargetLanguageCode()
        mainActivityViewModel.loadTargetLanguageTitle()
        loadAvailableLanguages()

        targetLanguageChooseBtn.text = mainActivityViewModel.getTargetLanguageTitle()


        startButton.setOnClickListener {
            val intent = Intent(this, Question1Activity::class.java)
            startActivity(intent)
            Log.d(TAG, "Going to Question1Activity & 1")
            setCurrentActivityIndex(this, 1)
        }

        targetLanguageChooseBtn.setOnClickListener {
            targetLanguageChoose()
            mainActivityViewModel.setCorrect1(0)
            mainActivityViewModel.setCorrect2(0)
            mainActivityViewModel.setCorrect3(0)
            mainActivityViewModel.setCorrect4(0)
        }
    }

    private fun loadAvailableLanguages() {
        languageArrayList = ArrayList()
        val languageCodeList = TranslateLanguage.getAllLanguages()
        for (languageCode in languageCodeList) {
            val languageTitle = Locale(languageCode).displayLanguage
            Log.d(TAG, "languageCode: $languageCode")
            Log.d(TAG, "languageTitle: $languageTitle")

            val modelLanguage = ModelLanguage(languageCode, languageTitle)

            languageArrayList!!.add(modelLanguage)
        }
    }

    private fun targetLanguageChoose() {
        val popupMenu = PopupMenu(this, targetLanguageChooseBtn)
        for (i in languageArrayList!!.indices) {
            popupMenu.menu.add(Menu.NONE, i, i, languageArrayList!![i].languageTitle)

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { menuItem ->

                val position = menuItem.itemId

                mainActivityViewModel.setTargetLanguageCode(languageArrayList!![position].languageCode)
                mainActivityViewModel.setTargetLanguageTitle(languageArrayList!![position].languageTitle)

                var targetLanguageCode = mainActivityViewModel.getTargetLanguageCode()
                var targetLanguageTitle = mainActivityViewModel.getTargetLanguageTitle()

                targetLanguageChooseBtn.text = targetLanguageTitle

                Log.d(TAG, "targetLanguageCode: $targetLanguageCode")
                Log.d(TAG, "targetLanguageTitle: $targetLanguageTitle")

                false
            }
        }
    }

    companion object {
        private const val PREFS_LAST_ACTIVITY_INDEX = "prefs_last_activity_index"

        fun getCurrentActivityIndex(context: Context): Int {
            val prefs = context.getSharedPreferences("default", Context.MODE_PRIVATE)
            return prefs.getInt(PREFS_LAST_ACTIVITY_INDEX, 0)
        }

        fun setCurrentActivityIndex(context: Context, index: Int) {
            val prefs = context.getSharedPreferences("default", Context.MODE_PRIVATE)
            prefs.edit().putInt(PREFS_LAST_ACTIVITY_INDEX, index).apply()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentIndex = getCurrentActivityIndex(this)
        Log.d(TAG, "Current Index: $currentIndex")
        when (currentIndex) {
            1 -> {
                val intent = Intent(this, Question1Activity::class.java)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, Question2Activity::class.java)
                startActivity(intent)
            }
            3 -> {
                val intent = Intent(this, Question3Activity::class.java)
                startActivity(intent)
            }
            4 -> {
                val intent = Intent(this, Question4Activity::class.java)
                startActivity(intent)
            }
            5 -> {
                val intent = Intent(this, QuizResultsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}