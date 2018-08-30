package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    var mFirebaseAuth = FirebaseAuth.getInstance()

    var mAuthStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        Log.d(TAG, "mAuthStateListener initialized")
        val firebaseUser = it.currentUser

        if (firebaseUser != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in: " + firebaseUser.uid)
            val intent = Intent(this, SignedInActivity::class.java)
            intent.putExtra(USER_EMAIL, firebaseUser.email)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {

            if (register_name_edit_text.text.toString().isBlank() ||
                    register_email_edit_text.text.toString().isBlank() ||
                    register_password_edit_text.text.toString().isBlank()) {

                Toast.makeText(this, "You can't leave any field empty", Toast.LENGTH_SHORT).show()
            } else {

                val name = register_name_edit_text.text.toString()
                val email = register_email_edit_text.text.toString()
                val password = register_password_edit_text.text.toString()

                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Register is Successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Register has Failed", Toast.LENGTH_SHORT).show()
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
