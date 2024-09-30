package com.example.emoji.model

import com.google.firebase.Timestamp

data class Users (
    val displayName: String = "",
    val emojis: String = "",
    val timestamp: Timestamp? = null
)