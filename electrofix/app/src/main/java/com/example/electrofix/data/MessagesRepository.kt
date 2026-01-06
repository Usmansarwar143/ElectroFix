package com.example.electrofix.data

import javax.inject.Inject

data class MessageThread(
    val id: String,
    val technicianName: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int
)

class MessagesRepository @Inject constructor() {
    fun getMessageThreads(): List<MessageThread> {
        return listOf(
            MessageThread("1", "John Doe", "See you tomorrow!", "10:45 AM", 2),
            MessageThread("2", "Jane Smith", "Thanks for the help!", "Yesterday", 0),
            MessageThread("3", "Peter Jones", "I have a question.", "2 days ago", 1)
        )
    }
}