package io.github.yahiaabdelwahab.ama.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.yahiaabdelwahab.ama.R
import io.github.yahiaabdelwahab.ama.`interface`.OnQuestionClickHandler

class QuestionsAdapter(val onQuestionClickHandler: OnQuestionClickHandler) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    var mQuestionsList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.question_list_item, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mQuestionsList.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.mQuestionTextView.text = mQuestionsList[position]
    }

    fun swapData(questionsList: MutableList<String>) {
        if (questionsList.size > 0) {
            mQuestionsList = questionsList
            notifyDataSetChanged()
        }
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mQuestionTextView: TextView = itemView.findViewById(R.id.question_list_item_question)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val question = mQuestionsList[adapterPosition]
            onQuestionClickHandler.onQuestionClick(question)
        }
    }
}