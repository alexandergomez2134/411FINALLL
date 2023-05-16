package com.example.quizlingo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "Question3Activity"

class Question3Activity : AppCompatActivity() {

    private lateinit var previousButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.question_3))
        setContentView(R.layout.activity_question3)

        previousButton = findViewById(R.id.previousButton3)
        nextButton = findViewById(R.id.nextButton3)

        previousButton.setOnClickListener {
            val intent = Intent(this, Question2Activity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 2)
            Log.d(TAG, "Going back to page Question2Activity.")
        }
        nextButton.setOnClickListener {
            val intent = Intent(this, Question4Activity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 4)
            Log.d(TAG, "Going back to page Question4Activity.")
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