package io.github.yahiaabdelwahab.ama.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.yahiaabdelwahab.ama.R
import io.github.yahiaabdelwahab.ama.`interface`.OnUserFollowedClickHandler
import io.github.yahiaabdelwahab.ama.model.User

class UsersFollowedAdapter(val onUserFollowedClickHandler: OnUserFollowedClickHandler) : RecyclerView.Adapter<UsersFollowedAdapter.UsersFollowedViewHolder>() {
    var mUsersFollowedList: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersFollowedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_followed_list_item, parent, false)
        return UsersFollowedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mUsersFollowedList.size
    }

    override fun onBindViewHolder(holder: UsersFollowedViewHolder, position: Int) {
        val currentUser = mUsersFollowedList[position]
        holder.mNameTextView.text = currentUser.name
        holder.mLocationTextView.text = currentUser.location
    }

    fun swapData(userFollowedList: MutableList<User>) {
        if (userFollowedList.size > 0) {
            mUsersFollowedList = userFollowedList
            notifyDataSetChanged()
        }
    }

    inner class UsersFollowedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mNameTextView: TextView = itemView.findViewById(R.id.user_followed_list_item_name_text_view)
        val mLocationTextView: TextView = itemView.findViewById(R.id.user_followed_list_item_location_text_view)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val currentUser = mUsersFollowedList[adapterPosition]
            onUserFollowedClickHandler.onUserFollowedClick(currentUser)
        }

    }
}