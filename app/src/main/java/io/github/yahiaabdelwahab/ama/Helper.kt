package io.github.yahiaabdelwahab.ama

import com.google.firebase.auth.FirebaseUser

public class Helper {
    companion object {
        fun isSignUpComplete(firebaseUser: FirebaseUser?): Boolean {
            var value = true
            if (firebaseUser!!.displayName == null || firebaseUser.displayName!!.isBlank()) {
                value = false
            }
            return value
        }
    }
}