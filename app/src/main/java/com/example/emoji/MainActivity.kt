package com.example.emoji

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private lateinit var btSignOut: Button
    private lateinit var rvUser: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<Users>
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btSignOut = findViewById(R.id.btSignOut)
        rvUser = findViewById(R.id.recyclerView)

        rvUser.layoutManager = LinearLayoutManager(this)

        userList = mutableListOf()
        adapter = UserAdapter(userList)
        rvUser.adapter = adapter

        fetchUsers()

        btSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val logOutIntent = Intent(this, LoginActivity::class.java)
            logOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logOutIntent)
            finish()
        }
    }

    private fun fetchUsers() {
        firestore.collection("users")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Something went wrong: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    userList.clear() // Clear the list to prevent duplicate entries
                    for (document in snapshot.documents) {
                        val user = Users(
                            displayName = document.getString("displayName") ?: "",
                            emojis = document.getString("emojis") ?: ""
                        )
                        userList.add(user)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }
}