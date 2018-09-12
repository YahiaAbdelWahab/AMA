package io.github.yahiaabdelwahab.ama.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.yahiaabdelwahab.ama.`interface`.OnUserClickHandler

import io.github.yahiaabdelwahab.ama.R
import io.github.yahiaabdelwahab.ama.model.User

class SearchAdapter(val onUserClickHandler: OnUserClickHandler) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var mSearchedUserList: MutableList<User> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_search_list_item, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentUser = mSearchedUserList[position]
        holder.mUserNameTextView.text = currentUser.name
    }

    override fun getItemCount(): Int {
        return mSearchedUserList.size
    }

    fun swapData(searchedUserList: MutableList<User>) {
        if (searchedUserList.size > 0) {
            mSearchedUserList = searchedUserList
            notifyDataSetChanged()
        }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mUserNameTextView: TextView = itemView.findViewById(R.id.search_item_name_text_view)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val user = mSearchedUserList[adapterPosition]
            onUserClickHandler.onUserClick(user)
        }
    }
}
