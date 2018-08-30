package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*


val USER_NAME = "user_name"
val USER_EMAIL = "user_email"

class SignInActivity : AppCompatActivity() {


    val TAG = "SignInActivity"

    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var mAuthStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        Log.d(TAG, "mAuthStateListener initialized")
        val firebaseUser = it.currentUser

        if (firebaseUser != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in: " + firebaseUser.uid)
            val intent = Intent(this, SignedInActivity::class.java)
            intent.putExtra(USER_NAME, firebaseUser.displayName)
            intent.putExtra(USER_EMAIL, firebaseUser.email)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        Log.i(TAG, "onCreate() called");

        sign_in_button.setOnClickListener {
            if (sign_in_email_edit_text.text.toString().isBlank() || sign_in_password_edit_text.text.toString().isBlank()) {
                Toast.makeText(this, "You can't leave any field empty", Toast.LENGTH_SHORT).show()
            } else {
                val email = sign_in_email_edit_text.text.toString()
                val password = sign_in_password_edit_text.text.toString()
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)

    }

    override fun onStop() {
        super.onStop()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }
}
