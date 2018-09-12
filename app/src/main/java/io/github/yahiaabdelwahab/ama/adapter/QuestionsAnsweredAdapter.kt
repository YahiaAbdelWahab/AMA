package io.github.yahiaabdelwahab.ama.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.yahiaabdelwahab.ama.R
import io.github.yahiaabdelwahab.ama.model.Answer

class QuestionsAnsweredAdapter : RecyclerView.Adapter<QuestionsAnsweredAdapter.QuestionsAnsweredViewHolder>() {

    var mQuestionsAnsweredList: MutableList<Answer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsAnsweredViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.question_answered_list_item, parent, false)
        return QuestionsAnsweredViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mQuestionsAnsweredList.size
    }

    override fun onBindViewHolder(holder: QuestionsAnsweredViewHolder, position: Int) {
        holder.mQuestionTextView.text = mQuestionsAnsweredList[position].question
        holder.mAnswerTextView.text = mQuestionsAnsweredList[position].answer
    }

    fun swapData(questionsAnsweredList: MutableList<Answer>) {
        if (questionsAnsweredList.size > 0) {
            mQuestionsAnsweredList = questionsAnsweredList
            notifyDataSetChanged()
        }
    }

    inner class QuestionsAnsweredViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mQuestionTextView: TextView = itemView.findViewById(R.id.question_answered_list_item_question)
        var mAnswerTextView: TextView = itemView.findViewById(R.id.question_answered_list_item_answer)

    }
}