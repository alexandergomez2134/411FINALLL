package com.example.quizlingo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "Question1Activity"

class Question1Activity : AppCompatActivity() {

    private lateinit var previousButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.question_1))
        setContentView(R.layout.activity_question1)

        previousButton = findViewById(R.id.previousButton1)
        nextButton = findViewById(R.id.nextButton1)

        previousButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 0)
            Log.d(TAG, "Going back to MainActivity.")

        }
        nextButton.setOnClickListener {
            val intent = Intent(this, Question2Activity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 2)
            Log.d(TAG, "Going back to page Question2Activity.")
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