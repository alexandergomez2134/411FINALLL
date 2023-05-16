package com.example.quizlingo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "Question2Activity"

class Question2Activity : AppCompatActivity() {

    private lateinit var previousButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.question_2))
        setContentView(R.layout.activity_question2)

        previousButton = findViewById(R.id.previousButton2)
        nextButton = findViewById(R.id.nextButton2)

        previousButton.setOnClickListener {
            val intent = Intent(this, Question1Activity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 1)
            Log.d(TAG, "Going back to page Question1Activity.")
        }
        nextButton.setOnClickListener {
            val intent = Intent(this, Question3Activity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 3)
            Log.d(TAG, "Going back to page Question3Activity.")
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