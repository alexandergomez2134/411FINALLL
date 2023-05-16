package com.example.quizlingo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.quizlingo.api.UnsplashExecutor

class PhotoAPIFragment : Fragment() {

    private lateinit var photoView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.photoapi_fragment, container, false)
        this.photoView = view.findViewById(R.id.photo_view)

        val currentActivityIndex = MainActivity.getCurrentActivityIndex(requireContext())
        Log.d("PhotoAPIFragmentActivity", "Current activity index: $currentActivityIndex")

        val wordBank = WordBank().wordBank
        val dbHelper = QuizDatabaseHelper(requireContext())
        dbHelper.addWords(wordBank)

//        val wordBank = WordBank()
//        val wordArray = wordBank.wordBank
//        val query = wordArray.getOrNull(currentActivityIndex - 1) ?: "dog"
        val words = dbHelper.getWordsFromDatabase()
        val word = words[currentActivityIndex - 1]
//        val query = wordBank.getOrNull(currentActivityIndex - 1) ?: "dog"
        val query = word ?: "dog"
        Log.d("PhotoAPIFragmentActivity", "Query: $query")
        val unsplashExecutor = UnsplashExecutor(photoView)
        unsplashExecutor.searchPhotos(query)

        return view
    }
}