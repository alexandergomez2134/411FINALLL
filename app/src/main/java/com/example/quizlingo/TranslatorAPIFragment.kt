package com.example.quizlingo

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale
import java.util.*

private const val TAG = "TranslatorAPIActivity"

class TranslatorAPIFragment : Fragment() {

    private lateinit var sourceLanguageEt: EditText // User input (English)
    private lateinit var targetLanguageTv: TextView // Displays correct answer
    private lateinit var submitButton: Button

    private lateinit var translatorOptions: TranslatorOptions
    private lateinit var translator: Translator

    private lateinit var progressDialog: ProgressDialog

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        AppPreferencesRepository.initialize(requireContext())
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    private var translatedText = ""
    private var sourceLanguageText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.translatorapi_fragment, container, false)

        sourceLanguageEt = view.findViewById(R.id.sourceLanguageEt)
        targetLanguageTv = view.findViewById(R.id.targetLanguageTv)
        submitButton = view.findViewById(R.id.submitButton)

        mainActivityViewModel.loadTargetLanguageCode()
        mainActivityViewModel.loadTargetLanguageTitle()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

//        // TODO: Fix EditText persistence
//        val sharedPref = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
//
//        sourceLanguageEt.setText(sharedPref.getString("myEditTextValue", ""))
//
//        sourceLanguageEt.addTextChangedListener(object: TextWatcher{
//            override fun afterTextChanged(p0: Editable?) {
//                val editor = sharedPref.edit()
//                editor.putString("myEditTextValue", p0.toString())
//                editor.apply()
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//        })

        submitButton.setOnClickListener {
            validateData(
                mainActivityViewModel.getTargetLanguageCode(),
                mainActivityViewModel.getSourceLanguageCode()
            )
            // Disables EditText after clicking submit button
            sourceLanguageEt.isEnabled = false
        }

        return view
    }

    private fun validateData(sourceLanguageCode: String?, targetLanguageCode: String?) {
        sourceLanguageText = sourceLanguageEt.text.toString().trim()

        Log.d(TAG, "validateData: sourceLanguageText: $sourceLanguageText")

        if (sourceLanguageText.isEmpty()) {
            showToast("Enter text to translate...")
        } else {
            startTranslation(sourceLanguageCode, targetLanguageCode)
            Log.d(TAG, "Source: $sourceLanguageCode, Target: $targetLanguageCode")
        }
    }

    private fun startTranslation(sourceLanguageCode: String?, targetLanguageCode: String?) {
        //progressDialog.setMessage("Processing language model...")
        progressDialog.setMessage("Checking your answer...")
        progressDialog.show()
        if (sourceLanguageCode != null && targetLanguageCode != null) {
            translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(targetLanguageCode)
                .build()
        }
        translator = Translation.getClient(translatorOptions)

        val downloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener {
                Log.d(TAG, "startTranslation: model ready, start translation...")

                progressDialog.setMessage("Translating...")

                translator.translate(sourceLanguageText)
                    .addOnSuccessListener { translatedText ->
//                        Log.d(TAG, "startTranslation: translatedText: $translatedText")
//
//                        progressDialog.dismiss()
//
//                        targetLanguageTv.text = translatedText
//                        val input: String? = sourceLanguageText
                        Log.d(TAG, "startTranslation: translatedText: $translatedText")

                        this.translatedText = translatedText
                        Log.d(TAG, "This is ${this.translatedText}")

                        progressDialog.dismiss()

                        targetLanguageTv.text = translatedText
                        val input: String? = sourceLanguageText

                        // Compare the translated text with the words in WordBank.kt
                        val currentActivityIndex =
                            MainActivity.getCurrentActivityIndex(requireContext())

                        Log.d(TAG, "This is translating to $targetLanguageCode")
//                        val wordBank = WordBank()
//                        val wordArray = wordBank.wordBank
                        //we only want it work like that if the language ur learning is english
                        // if learning english false & learning spanish true && translated == word, correct?
                        // if its not equal to english and translatedword == word WRONG
                        val wordBank = WordBank().wordBank
                        val dbHelper = QuizDatabaseHelper(requireContext())
                        dbHelper.addWords(wordBank)
                        val words = dbHelper.getWordsFromDatabase()
                        val word = words[currentActivityIndex - 1]

                        if ((word == translatedText.lowercase(Locale.getDefault()) && sourceLanguageText != word) || (word == translatedText.lowercase(
                                Locale.getDefault()
                            ) && sourceLanguageCode == "en" && targetLanguageCode == "en")) {
                            // Match found
                            showToast("Correct Answer!")
                            when (currentActivityIndex) {
                                1 -> {
                                    mainActivityViewModel.setCorrect1(1)
                                }
                                2 -> {
                                    mainActivityViewModel.setCorrect2(1)
                                }
                                3 -> {
                                    mainActivityViewModel.setCorrect3(1)
                                }
                                4 -> {
                                    mainActivityViewModel.setCorrect4(1)
                                }
                            }

                        } else {
                            // Match not found
                            showToast("Incorrect Answer!")
                            when (currentActivityIndex) {
                                1 -> {
                                    mainActivityViewModel.setCorrect1(0)
                                }
                                2 -> {
                                    mainActivityViewModel.setCorrect2(0)
                                }
                                3 -> {
                                    mainActivityViewModel.setCorrect3(0)
                                }
                                4 -> {
                                    mainActivityViewModel.setCorrect4(0)
                                }
                            }
                        }
//                        if(wordArray[currentActivityIndex - 1] == translatedText.toLowerCase()) {
//                            // Match found
//                            showToast("Correct Answer!")
//                            if (currentActivityIndex == 1) {
//                                mainActivityViewModel.setCorrect1(1)
//                            } else if (currentActivityIndex == 2) {
//                                mainActivityViewModel.setCorrect2(1)
//                            } else if (currentActivityIndex == 3) {
//                                mainActivityViewModel.setCorrect3(1)
//                            } else if (currentActivityIndex == 4) {
//                                mainActivityViewModel.setCorrect4(1)
//                            }
//
//                        } else {
//                            // Match not found
//                            showToast("Incorrect Answer!")
//                            if (currentActivityIndex == 1) {
//                                mainActivityViewModel.setCorrect1(0)
//                            } else if (currentActivityIndex == 2) {
//                                mainActivityViewModel.setCorrect2(0)
//                            } else if (currentActivityIndex == 3) {
//                                mainActivityViewModel.setCorrect3(0)
//                            } else if (currentActivityIndex == 4) {
//                                mainActivityViewModel.setCorrect4(0)
//                            }
//                        }
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Log.d(TAG, "startTranslation: ", e)

                        showToast("Failed to translate due to ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.d(TAG, "startTranslation: ", e)

                showToast("Failed due to ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}