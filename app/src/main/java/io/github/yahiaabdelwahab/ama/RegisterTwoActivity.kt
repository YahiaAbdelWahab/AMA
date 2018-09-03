package io.github.yahiaabdelwahab.ama

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_two.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest



const val USERS_COL = "users"
const val USER_DOC_ID = "id"
const val USER_DOC_NAME = "name"
const val USER_DOC_EMAIL = "email"
const val USER_DOC_LOCATION = "location"
const val USER_DOC_BIO = "bio"


class RegisterTwoActivity : AppCompatActivity() {


    val TAG = "RegisterTwoActivity"


    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_two)

        continue_two_button.setOnClickListener {
            if (register_name_edit_text.text.toString().isBlank() ||
                    register_location_edit_text.text.toString().isBlank() ||
                    register_bio_edit_text.text.toString().isBlank()) {
                Toast.makeText(this, "You can't leave any field empty", Toast.LENGTH_SHORT).show()
            } else {
                val name = register_name_edit_text.text.toString()
                val location = register_location_edit_text.text.toString()
                val bio = register_bio_edit_text.text.toString()

                val user = FirebaseAuth.getInstance().currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener {
                            Log.d(TAG, "User profile updated.");

                        }

                val userMap = HashMap<String, Any>()
                userMap.put(USER_DOC_ID, user.uid)
                userMap.put(USER_DOC_NAME, name)
                userMap.put(USER_DOC_EMAIL, user.email!!)
                userMap.put(USER_DOC_LOCATION, location)
                userMap.put(USER_DOC_BIO, bio)

                db.collection(USERS_COL).document("user_" + user.uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!");

                        }
                        .addOnFailureListener {
                            Log.w(TAG, "Error adding document", it)

                        }

                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        }
    }


//    override fun onBackPressed() {
//
//    }

}
