package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.firebase.auth.UserProfileChangeRequest



class SettingsActivity : AppCompatActivity() {

    private val ActivityIndex = 3

    val mAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupBottomNavigation(settings_bottom_nav)

        update_your_name_button.setOnClickListener {
            if (update_your_name_edit_text.text.isBlank()) {
                Toast.makeText(this, "Please Enter Your New Name", Toast.LENGTH_SHORT).show()
            } else {
                val newName = update_your_name_edit_text.text.toString()
                val user = mAuth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()
                user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Name Updated", Toast.LENGTH_SHORT).show()
                            }
                        }

                val newNameMap = HashMap<String, Any>()
                newNameMap.put(USER_DOC_NAME, newName)
                db.collection(USERS_COLLECTION)
                        .whereEqualTo(USER_DOC_ID, mAuth.currentUser!!.uid)
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (userDocument in it.result) {
                                    userDocument.reference.update(newNameMap)
                                }
                            }
                        }
            }
        }

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
                R.id.action_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.action_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.action_questions -> {
                    startActivity(Intent(this, QuestionsActivity::class.java))
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
        settings_bottom_nav.menu.getItem(ActivityIndex).setChecked(true);
    }
}
