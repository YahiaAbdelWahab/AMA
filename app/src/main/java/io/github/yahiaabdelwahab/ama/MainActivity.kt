package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private val ActivityIndex = 0;

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation(home_bottom_nav)

        if (mAuth.currentUser != null) {
            textView.text = "Hello, " + mAuth.currentUser!!.displayName
        }

        sign_out_button.setOnClickListener {
            if (mAuth.currentUser != null) {
                mAuth.signOut()
                val intent = Intent(this, RegisterOneActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
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
        home_bottom_nav.menu.getItem(ActivityIndex).setChecked(true);
    }
}
