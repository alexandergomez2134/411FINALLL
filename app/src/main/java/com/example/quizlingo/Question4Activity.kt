package com.example.quizlingo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "Question4Activity"

class Question4Activity : AppCompatActivity() {

    private lateinit var previousButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.question_4))
        setContentView(R.layout.activity_question4)

        previousButton = findViewById(R.id.previousButton4)
        nextButton = findViewById(R.id.nextButton4)

        nextButton.text = getString(R.string.results_page)

        previousButton.setOnClickListener {
            val intent = Intent(this, Question3Activity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 3)
            Log.d(TAG, "Going back to page Question3Activity.")
        }
        nextButton.setOnClickListener {
            val intent = Intent(this, QuizResultsActivity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 5)
            Log.d(TAG, "Going to page QuizResultsActivity.")
        }

        val photoAPIFragment =
            this.supportFragmentManager.findFragmentById(R.id.photofragment_container)
        if (photoAPIFragment == null) {
            val fragment = PhotoAPIFragment()
            this.supportFragmentManager
                .beginTransaction()
                .add(R.id.photofragment_container, fragment)
                .commit()
        }

        val translatorAPIFragment =
            this.supportFragmentManager.findFragmentById(R.id.translatorfragment_container)
        if (translatorAPIFragment == null) {
            val fragment = TranslatorAPIFragment()
            this.supportFragmentManager
                .beginTransaction()
                .add(R.id.translatorfragment_container, fragment)
                .commit()
        }
    }
}