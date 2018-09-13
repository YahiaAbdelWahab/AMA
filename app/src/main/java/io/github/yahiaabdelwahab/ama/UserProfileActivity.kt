package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import io.github.yahiaabdelwahab.ama.model.User
import kotlinx.android.synthetic.main.activity_user_profile.*
import com.google.firebase.firestore.FirebaseFirestore
import io.github.yahiaabdelwahab.ama.adapter.QuestionsAnsweredAdapter
import io.github.yahiaabdelwahab.ama.model.Answer


class UserProfileActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    val mAuth = FirebaseAuth.getInstance()

    val TAG = "UserProfileActivity"

    lateinit var userDisplayed: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        val intent = intent
        if (intent != null && intent.extras.containsKey(user_profile_activity_extra)) {

            // Update the displayed user's name, location and bio
            userDisplayed = intent.extras.get(user_profile_activity_extra) as User
            title = userDisplayed.name + "'s profile"
            user_profile_name.text = userDisplayed.name
            user_profile_location_text_view.text = userDisplayed.location
            user_profile_bio_text_view.text = userDisplayed.bio

            // Update the Follow/Following button
            db.collection(USERS_COLLECTION)
                    .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            for (document in it.result) {
                                document.reference.collection(USERS_FOLLOWED_COLLECTION)
                                        .whereEqualTo(FOLLOWED_DOC_UID, userDisplayed.id)
                                        .get()
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                for (userFollowed in it.result) {
                                                    if (userFollowed.get(FOLLOWED_DOC_UID) == userDisplayed.id) {
                                                        buttonFollowingStyle(user_profile_follow_button)
                                                    }
                                                }
                                                user_profile_follow_progress_bar.visibility = View.INVISIBLE
                                                user_profile_follow_button.visibility = View.VISIBLE
                                            }
                                        }
                            }
                        }
                    }

        }

        user_profile_ask_button.setOnClickListener {

            user_profile_ask_progress_bar.visibility = View.VISIBLE
            user_profile_ask_button.visibility = View.INVISIBLE

            if (user_profile_ask_question_edit_text.text.toString().isBlank()) {
                Toast.makeText(this, "You have to ask a question first", Toast.LENGTH_SHORT).show()
            } else if (userDisplayed != null) {
                val documentName = "user_" + userDisplayed.id

                val questionAskedMap = HashMap<String, Any>()
                questionAskedMap.put(QUESTION_DOC_QUESTION, user_profile_ask_question_edit_text.text.toString())

                db.collection(USERS_COLLECTION)
                        .whereEqualTo(USER_DOC_ID, userDisplayed.id)
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (userDocument in it.result) {
                                    userDocument.reference.collection(QUESTIONS_ASKED_COLLECTION)
                                            .add(questionAskedMap)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "Question Sent", Toast.LENGTH_SHORT).show()
                                                user_profile_ask_question_edit_text.text.clear()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "Asking Question Failed", Toast.LENGTH_SHORT).show()
                                            }
                                }

                                user_profile_ask_progress_bar.visibility = View.INVISIBLE
                                user_profile_ask_button.visibility = View.VISIBLE
                            }
                        }
            }
        }

        user_profile_follow_button.setOnClickListener {
            if (userDisplayed != null) {
                if (user_profile_follow_button.text.toString() == getString(R.string.follow)) {
                    // No need to check if the currentUser is null, because only authenticated user can access SearchActivity
                    val documentName = "user_" + mAuth.currentUser!!.uid

                    val selectedUserToBeFollowedMap = HashMap<String, Any>()
                    selectedUserToBeFollowedMap.put(FOLLOWED_DOC_UID, userDisplayed.id!!)

                    db.collection(USERS_COLLECTION)
                            .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                            .get()
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    for (userDocument in it.result) {
                                        userDocument.reference.collection(USERS_FOLLOWED_COLLECTION)
                                                .add(selectedUserToBeFollowedMap)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this, "User Followed", Toast.LENGTH_SHORT).show()
                                                    buttonFollowingStyle(user_profile_follow_button)
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(this, "Following user failed", Toast.LENGTH_SHORT).show()
                                                    buttonFollowStyle(user_profile_follow_button)
                                                }
                                    }
                                }
                            }

                } else if (user_profile_follow_button.text.toString() == getString(R.string.following)) {
                    db.collection(USERS_COLLECTION)
                            .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                            .get()
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    for (userDocument in it.result) {
                                        userDocument.reference.collection(USERS_FOLLOWED_COLLECTION)
                                                .whereEqualTo(FOLLOWED_DOC_UID, userDisplayed.id)
                                                .get()
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        for (document in it.result) {
                                                            document.reference.delete()
                                                                    .addOnSuccessListener {
                                                                        Toast.makeText(this, "User Unfollowed", Toast.LENGTH_SHORT).show()
                                                                        buttonFollowStyle(user_profile_follow_button)
                                                                    }
                                                                    .addOnFailureListener {
                                                                        Toast.makeText(this, "Unfollowing this user failed", Toast.LENGTH_SHORT).show()
                                                                        buttonFollowingStyle(user_profile_follow_button)
                                                                    }
                                                        }
                                                    }
                                                }
                                    }
                                }
                            }

                }
            }
        }

    }

    private fun getAnsweredQuestions(user: User) {
        val questionsAnsweredAdapter = QuestionsAnsweredAdapter()

        user_profile_questions_answered_recycler_view.apply {
            adapter = questionsAnsweredAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }

        val questionsAnsweredList: MutableList<Answer> = mutableListOf()

        db.collection(USERS_COLLECTION)
                .whereEqualTo(USER_DOC_ID, user.id)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (userDocument in it.result) {
                            userDocument.reference.collection(QUESTIONS_ANSWERED_COLLECTION)
                                    .get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            for (answerDocument in it.result) {
                                                val questionString = answerDocument.get(ANSWER_DOC_QUESTION) as String
                                                val answerString = answerDocument.get(ANSWER_DOC_ANSWER) as String
                                                val answer: Answer = Answer(questionString, answerString)
                                                questionsAnsweredList.add(answer)
                                            }
                                            questionsAnsweredAdapter.swapData(questionsAnsweredList)
                                        }
                                    }
                        }
                    }
                }
    }


    private fun buttonFollowingStyle(button: Button) {
        button.background = getDrawable(R.drawable.following_button)
        button.setTextColor(Color.WHITE)
        button.text = getString(R.string.following)
    }

    private fun buttonFollowStyle(button: Button) {
        button.background = getDrawable(R.drawable.round_button)
        button.setTextColor(Color.parseColor("#00BCD4"))
        button.text = getString(R.string.follow)
    }


    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null) {
            startActivity(Intent(this, RegisterOneActivity::class.java))
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        getAnsweredQuestions(userDisplayed)
    }
}
