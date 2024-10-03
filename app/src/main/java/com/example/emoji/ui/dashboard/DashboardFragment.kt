package com.example.emoji.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.LoginActivity
import com.example.emoji.R
import com.example.emoji.UserAdapter
import com.example.emoji.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DashboardFragment : Fragment() {

//    private lateinit var btSignOut: Button
    private lateinit var rvUser: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<Users>
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

//        btSignOut = view.findViewById(R.id.btSignOut)
        rvUser = view.findViewById(R.id.recyclerView)

        rvUser.layoutManager = LinearLayoutManager(requireContext())

        userList = mutableListOf()
        adapter = UserAdapter(userList)
        rvUser.adapter = adapter

        fetchUsers()

//        btSignOut.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            val logOutIntent = Intent(requireContext(), LoginActivity::class.java)
//            logOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(logOutIntent)
//            requireActivity().finish()
//        }

        return view
    }

    private fun fetchUsers() {
        firestore.collection("users")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(requireContext(), "Something went wrong: ${exception.message}", Toast.LENGTH_SHORT).show()
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
