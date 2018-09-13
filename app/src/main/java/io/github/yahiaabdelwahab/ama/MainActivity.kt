package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.yahiaabdelwahab.ama.`interface`.OnUserFollowedClickHandler
import io.github.yahiaabdelwahab.ama.adapter.UsersFollowedAdapter
import io.github.yahiaabdelwahab.ama.model.User
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), OnUserFollowedClickHandler {

    private val ActivityIndex = 0;

    val mAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation(home_bottom_nav)

        sign_out_button.setOnClickListener {
            if (mAuth.currentUser != null) {
                mAuth.signOut()
                val intent = Intent(this, RegisterOneActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }


    private fun setupBottomNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.action_questions -> {
                    startActivity(Intent(this, QuestionsActivity::class.java))
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

    private fun getUsersFollowed() {
        val uidList: MutableList<String> = mutableListOf()

        val usersFollowedList: MutableList<User> = mutableListOf()

        val usersFollowedAdapter = UsersFollowedAdapter(this)

        home_recycler_view.apply {
            adapter = usersFollowedAdapter

            layoutManager = LinearLayoutManager(baseContext)
        }

        db.collection(USERS_COLLECTION)
                .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (document in it.result) {
                            document.reference.collection(USERS_FOLLOWED_COLLECTION)
                                    .get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {

                                            for (userFollowed in it.result) {
                                                home_progress_bar.visibility = View.VISIBLE
                                                home_recycler_view.visibility = View.INVISIBLE

                                                uidList.add(userFollowed.get(FOLLOWED_DOC_UID).toString())
                                            }

                                            for (userUid in uidList) {
                                                db.collection(USERS_COLLECTION)
                                                        .whereEqualTo(USER_DOC_ID, userUid)
                                                        .get()
                                                        .addOnCompleteListener {
                                                            if (it.isSuccessful) {
                                                                for (userDocument in it.result) {
                                                                    val name = userDocument.get(USER_DOC_NAME) as String
                                                                    val location = userDocument.get(USER_DOC_LOCATION) as String
                                                                    val bio = userDocument.get(USER_DOC_BIO) as String
                                                                    val email = userDocument.get(USER_DOC_EMAIL) as String
                                                                    val id = userDocument.get(USER_DOC_ID) as String
                                                                    val user = User(id, name, email, location, bio)
                                                                    usersFollowedList.add(user)
                                                                }
                                                                home_progress_bar.visibility = View.INVISIBLE
                                                                home_recycler_view.visibility = View.VISIBLE
                                                                usersFollowedAdapter.swapData(usersFollowedList)
                                                            }
                                                        }
                                            }
                                        }
                                    }
                        }
                    }
                }
    }

    override fun onUserFollowedClick(user: User) {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(user_profile_activity_extra, user)
        startActivity(intent)
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
        home_bottom_nav.menu.getItem(ActivityIndex).setChecked(true)
        getUsersFollowed()
    }
}
