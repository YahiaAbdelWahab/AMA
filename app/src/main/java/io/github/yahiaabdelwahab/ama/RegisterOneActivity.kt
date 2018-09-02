package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_one.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.UserProfileChangeRequest





class RegisterOneActivity : AppCompatActivity() {

    val TAG = "RegisterOneActivity"


    val mFirebaseAuth = FirebaseAuth.getInstance()


    var mAuthStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        Log.d(TAG, "mAuthStateListener initialized")
        val user = it.currentUser

        if (user != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in: " + user.uid)

            startActivity(Intent(this, RegisterTwoActivity::class.java))
            finish()
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_one)

        already_registered_button.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        continue_one_button.setOnClickListener {

            if (register_email_edit_text.text.toString().isBlank() ||
                    register_password_edit_text.text.toString().isBlank()) {

                Toast.makeText(this, "You can't leave any field empty", Toast.LENGTH_SHORT).show()

            } else {

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
