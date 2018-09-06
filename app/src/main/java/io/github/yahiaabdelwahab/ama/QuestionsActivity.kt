package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_questions.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.nio.file.Files.exists
import com.google.firebase.firestore.DocumentSnapshot
import io.github.yahiaabdelwahab.ama.`interface`.OnQuestionClickHandler
import io.github.yahiaabdelwahab.ama.adapter.QuestionsAdapter


class QuestionsActivity : AppCompatActivity(), OnQuestionClickHandler {

    private val ActivityIndex = 2

    val mAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        val questionAdapter = QuestionsAdapter(this)

        questions_recycler_view.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }

        getQuestionsAsked(questionAdapter)

        setupBottomNavigation(questions_bottom_nav)
    }


    override fun onQuestionClick(question: String) {

    }

    private fun getQuestionsAsked(questionsAdapter: QuestionsAdapter) {
        val questionsList: MutableList<String> = mutableListOf()

        db.collection(USERS_COLLECTION).document("user_" + mAuth.currentUser!!.uid)
                .collection(QUESTIONS_ASKED_COLLECTION)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (document in it.result) {
                            val question: String = document.get(QUESTION_DOC_QUESTION).toString()
                            questionsList.add(question)
                        }
                    }
                    if (questionsList.size > 0) {
                        questionsAdapter.swapData(questionsList)
                    }
                }
    }



    private fun setupBottomNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.action_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user == null) {
            startActivity(Intent(this, RegisterOneActivity::class.java))
            finish()
        } else if (!Helper.isSignUpComplete(user)) {
            startActivity(Intent(this, RegisterTwoActivity::class.java))
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        questions_bottom_nav.menu.getItem(ActivityIndex).setChecked(true);
    }
}
