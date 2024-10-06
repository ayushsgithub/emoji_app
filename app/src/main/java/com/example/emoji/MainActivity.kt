package com.example.emoji

import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.model.Users
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var btSignOut: Button
    private lateinit var btEdit: FloatingActionButton
    private lateinit var rvUser: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<Users>
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        btSignOut = findViewById(R.id.btSignOut)
        btEdit = findViewById(R.id.btEdit)
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

        btEdit.setOnClickListener {
            showAlertDialog()
        }
    }



    private fun showAlertDialog() {
        val editText = EditText(this)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Update your emojis")
            .setView(editText)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val emojisEntered = editText.text.toString()

            if (emojisEntered.isBlank()) {
                Toast.makeText(this, "Cannot submit empty text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Hide the keyboard
            hideKeyboard(editText)

            // Reference to the user document
            val userDocRef = firestore.collection("users").document(currentUser.uid)

            // Check if the document exists before updating
            userDocRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Document exists, so update it
                    userDocRef.update("emojis", emojisEntered, "timestamp", getCurrentTimestamp())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Emojis updated successfully!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error updating emojis: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Document does not exist, create it
                    val displayName = currentUser.displayName ?: "Anonymous" // Set displayName, default to "Anonymous" if null
                    val newUserData = mapOf(
                        "emojis" to emojisEntered,
                        "displayName" to displayName,
                        "timestamp" to getCurrentTimestamp() // Use the formatted timestamp here
                    )

                    userDocRef.set(newUserData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Emojis created successfully!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error creating document: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error checking document existence: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to get the current timestamp in a human-readable format
    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm:ss a z", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
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

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}
