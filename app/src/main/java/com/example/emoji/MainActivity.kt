package com.example.emoji

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var btSignOut: Button
    private lateinit var rvUser: RecyclerView
    private lateinit var userAdapter: UserAdapter
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
        userAdapter = UserAdapter(userList)
        rvUser.adapter = userAdapter

        fetchUsers()

        btSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Correctly get the FirebaseAuth instance
            val logOutIntent = Intent(this, LoginActivity::class.java)
            logOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logOutIntent)
            finish()
        }
    }

    private fun fetchUsers() {
        firestore.collection("users") // Change "users" to your Firestore collection name
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val user = Users(
                        displayName = document.getString("displayName") ?: "",
                        emojis = document.getString("emojis") ?: ""
                    )
                    userList.add(user)
                }
                userAdapter.notifyDataSetChanged() // Notify adapter about data change
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Something went wrong: ${exception.message}", Toast.LENGTH_SHORT).show() // Show a toast message with the error
            }
    }
}
