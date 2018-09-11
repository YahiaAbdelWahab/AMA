package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_answer_question.*

class AnswerQuestionActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_question)

        val intent = intent
        if (intent != null) {
            val question = intent.getStringExtra(QUESTION_ASKED_EXTRA)
            answer_question_question_text_view.text = question
            answer_question_answer_button.setOnClickListener {
                if (answer_question_answer_edit_text.text.toString().isBlank()) {
                    Toast.makeText(this, getString(R.string.type_answer_first), Toast.LENGTH_SHORT).show()
                } else {
                    val answer = answer_question_answer_edit_text.text.toString()
                    answerQuestion(question, answer)

                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        answer_question_answer_edit_text.text.clear()
                        finish()
                    }, 3000)

                }
            }
        }
    }

    private fun answerQuestion(question: String, answer: String): Boolean {
        var questionAnswered = false

        val answerMap = HashMap<String, Any>()
        answerMap.put(ANSWER_DOC_QUESTION, question)
        answerMap.put(ANSWER_DOC_ANSWER, answer)

        db.collection(USERS_COLLECTION)
                .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (userDocument in it.result) {
                            userDocument.reference.collection(QUESTIONS_ASKED_COLLECTION)
                                    .whereEqualTo(QUESTION_DOC_QUESTION, question)
                                    .get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            for (questionDocument in it.result) {
                                                questionDocument.reference.delete()
                                                        .addOnSuccessListener {
                                                            db.collection(USERS_COLLECTION)
                                                                    .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                                                                    .get()
                                                                    .addOnCompleteListener {
                                                                        if (it.isSuccessful) {
                                                                            for (userDocument2 in it.result) {
                                                                                userDocument2.reference.collection(QUESTIONS_ANSWERED_COLLECTION)
                                                                                        .add(answerMap)
                                                                                        .addOnSuccessListener {
                                                                                            questionAnswered = true
                                                                                            Toast.makeText(this, "Question Answered", Toast.LENGTH_SHORT).show()
                                                                                        }
                                                                                        .addOnFailureListener {
                                                                                            Toast.makeText(this, "Answering Question Failed", Toast.LENGTH_SHORT).show()
                                                                                        }
                                                                            }
                                                                        }
                                                                    }
                                                        }
                                                        .addOnFailureListener {
                                                            Toast.makeText(this, "Answering Question Failed", Toast.LENGTH_SHORT).show()
                                                        }
                                            }
                                        }
                                    }
                        }
                    }
                }

        return questionAnswered
    }
}
