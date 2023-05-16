package com.example.quizlingo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WordBank {
    val wordBank = arrayOf("dog", "cat", "apple", "house")
}

class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_QUERY)
        onCreate(db)
    }

    fun addWords(words: Array<String>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (word in words) {
                val values = ContentValues()
                values.put(COLUMN_WORD, word)
                db.insert(TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getWordsFromDatabase(): List<String> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_WORD), null, null, null, null, null)
        val words = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                words.add(getString(getColumnIndexOrThrow(COLUMN_WORD)))
            }
        }
        cursor.close()
        db.close()
        return words
    }

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "word_bank"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_WORD = "word"

        private const val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_WORD TEXT NOT NULL" +
                ")"

        private const val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
