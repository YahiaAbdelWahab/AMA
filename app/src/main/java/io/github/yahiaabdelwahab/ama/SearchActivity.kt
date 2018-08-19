package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {


    private val ActivityIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_bottom_nav.setOnNavigationItemSelectedListener {
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
        search_bottom_nav.menu.getItem(ActivityIndex).setChecked(true);
    }
}
