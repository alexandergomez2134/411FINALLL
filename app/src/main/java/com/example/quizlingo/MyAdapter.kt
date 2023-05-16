package com.example.quizlingo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private val questionsList : ArrayList<Questions>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = questionsList[position]
        val currentActivityIndex =
            MainActivity.getCurrentActivityIndex(requireContext())
        val wordBank = WordBank().wordBank
        val dbHelper = QuizDatabaseHelper(requireContext())
        dbHelper.addWords(wordBank)
        val words = dbHelper.getWordsFromDatabase()
        val word = words[currentActivityIndex - 1]
        holder.tvHeading.text = currentItem.heading
        holder.answer.text = currentItem.answerInfo

    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvHeading : TextView = itemView.findViewById(R.id.tvHeading)
        val answer : TextView = itemView.findViewById(R.id.correctOrIncorrect)

    }
}