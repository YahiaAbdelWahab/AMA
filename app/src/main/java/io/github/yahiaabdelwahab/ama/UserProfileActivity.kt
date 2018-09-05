package io.github.yahiaabdelwahab.ama

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.yahiaabdelwahab.ama.model.User
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val intent = intent
        if (intent.extras != null && intent.extras.containsKey(user_profile_activity_extra)) {
            val user: User = intent.extras.get(user_profile_activity_extra) as User
            title = user.name + "'s profile"
            user_profile_name.text = user.name
            user_profile_location_text_view.text = user.location
            user_profile_bio_text_view.text = user.bio
        }
    }
}
