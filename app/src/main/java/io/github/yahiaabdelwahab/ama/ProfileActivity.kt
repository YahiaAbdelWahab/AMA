package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val ActivityIndex = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profile_bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onResume() {
        super.onResume()
        profile_bottom_nav.menu.getItem(ActivityIndex).setChecked(true);
    }
}
