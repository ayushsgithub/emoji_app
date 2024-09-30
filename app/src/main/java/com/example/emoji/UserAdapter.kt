package com.example.emoji

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.model.Users

class UserAdapter(private val userList: List<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        val userName: TextView = holder.itemView.findViewById(R.id.userName)
        val userEmojis: TextView = holder.itemView.findViewById(R.id.userEmojis)

        userName.text = user.displayName ?: "Unknown User" // Handle null case
        userEmojis.text = user.emojis ?: "No Emojis" // Handle null case
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
