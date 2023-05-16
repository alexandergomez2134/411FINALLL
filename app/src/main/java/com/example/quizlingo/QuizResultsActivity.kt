package com.example.quizlingo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.security.identity.AccessControlProfileId
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "QuizResultsActivity"
const val CORRECT1 = "correct1"
const val CORRECT2 = "correct2"
const val CORRECT3 = "correct3"
const val CORRECT4 = "correct4"

class QuizResultsActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Questions>
    lateinit var heading : Array<String>
    lateinit var answerInfo : Array<String>

    private lateinit var scoreTextView: TextView
    private lateinit var resetButton: Button

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        AppPreferencesRepository.initialize(this)
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val celebrationSoundPlayer = MediaPlayer.create(this, R.raw.celebration_sound)
        val lossSoundPlayer = MediaPlayer.create(this, R.raw.loss_sound)

        scoreTextView = findViewById(R.id.score)
        resetButton = findViewById(R.id.resetButton)

        mainActivityViewModel.loadCorrect1()
        mainActivityViewModel.loadCorrect2()
        mainActivityViewModel.loadCorrect3()
        mainActivityViewModel.loadCorrect4()

        val score =
            mainActivityViewModel.getCorrect1() + mainActivityViewModel.getCorrect2() + mainActivityViewModel.getCorrect3() + mainActivityViewModel.getCorrect4()

        scoreTextView.text = score.toString()
        Log.d(TAG, "This is the total score: $score")

        if (score >= 3) {
            celebrationSoundPlayer.start()
            celebrationSoundPlayer.setOnCompletionListener {
                celebrationSoundPlayer.release()
            }
        } else {
            lossSoundPlayer.start()
            lossSoundPlayer.setOnCompletionListener {
                lossSoundPlayer.release()
            }
        }

        resetButton.setOnClickListener {
            mainActivityViewModel.setCorrect1(0)
            mainActivityViewModel.setCorrect2(0)
            mainActivityViewModel.setCorrect3(0)
            mainActivityViewModel.setCorrect4(0)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            MainActivity.setCurrentActivityIndex(this, 0)
            Log.d(TAG, "Going back to MainActivity.")
        }

        heading = arrayOf(
            "Dog", "Cat", "Apple", "House"
        )

        answerInfo = arrayOf(
            "Correct", "Incorrect", "Correct", "Incorrect"
        )


        newRecyclerView = findViewById(R.id.quizScoreDisplay)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Questions>()
        getUserData()
    }

    private fun getUserData() {
        for(i in heading.indices){
            val questions = Questions(answerInfo[i],heading[i])
            newArrayList.add(questions)
        }
        newRecyclerView.adapter = MyAdapter(newArrayList)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Store and retrieve, key-value pair
        outState.putInt(CORRECT1, mainActivityViewModel.getCorrect1())
        outState.putInt(CORRECT2, mainActivityViewModel.getCorrect2())
        outState.putInt(CORRECT3, mainActivityViewModel.getCorrect3())
        outState.putInt(CORRECT4, mainActivityViewModel.getCorrect4())
    }
}