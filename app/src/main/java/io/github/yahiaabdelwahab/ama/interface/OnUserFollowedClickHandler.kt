package io.github.yahiaabdelwahab.ama.`interface`

import io.github.yahiaabdelwahab.ama.model.User

interface OnUserFollowedClickHandler {
    fun onUserFollowedClick(user: User)
}