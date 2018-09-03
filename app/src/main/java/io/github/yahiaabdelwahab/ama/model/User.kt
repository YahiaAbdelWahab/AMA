package io.github.yahiaabdelwahab.ama.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val id: String? = null,
                val name: String,
                val email: String, val photoUrl: String,
                val location: String,
                val bio: String) : Parcelable