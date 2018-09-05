package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QueryDocumentSnapshot
import io.github.yahiaabdelwahab.ama.adapter.SearchAdapter
import io.github.yahiaabdelwahab.ama.model.User


val user_profile_activity_extra = "user_profile_activity_extra"

class SearchActivity : AppCompatActivity(), OnUserClickHandler {

    private val TAG = "SearchActivity"
    private val ActivityIndex = 1


    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupBottomNavigation(search_bottom_nav)

        val searchAdapter = SearchAdapter(this)

        search_recycler_view.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(baseContext)
        }

        submit_search_button.setOnClickListener {
            if (search_edit_text.text.toString().isBlank()) {
                Toast.makeText(this, "Type a query first", Toast.LENGTH_SHORT).show()
            } else {
                val search = search_edit_text.text.toString()
                val userList = mutableListOf<User>()

                db.collection(USERS_COL)
                        .whereEqualTo(USER_DOC_NAME, search.trim())
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (document in it.result) {
                                    if (document.data.containsKey(USER_DOC_ID) && document.data.containsKey(USER_DOC_NAME)) {
                                        val id = document.get(USER_DOC_ID).toString()
                                        val name = document.get(USER_DOC_NAME).toString()
                                        val email = document.get(USER_DOC_EMAIL).toString()
                                        val location = document.get(USER_DOC_LOCATION).toString()
                                        val bio = document.get(USER_DOC_BIO).toString()

                                        val user = User(id, name, email, location, bio)
                                        userList.add(user)
                                    }
                                }
                                if (userList.size > 0) {
                                    searchAdapter.swapData(userList)
                                }
                            }
                        }

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
                R.id.action_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
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


    override fun onUserClick(user: User) {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(user_profile_activity_extra, user)
        startActivity(intent)
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
        search_bottom_nav.menu.getItem(ActivityIndex).setChecked(true);
    }
}
