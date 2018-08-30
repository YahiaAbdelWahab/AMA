package io.github.yahiaabdelwahab.ama

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_signed_in.*

class SignedInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)

        val intent = intent
        val email = intent.getStringExtra(USER_EMAIL)
        email_value.setText(email)
    }
}
