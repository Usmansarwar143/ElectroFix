package com.example.electrofix.data

import com.example.electrofix.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createUser(uid: String, email: String, role: String) {
        val user = User(
            uid = uid,
            email = email,
            role = role
        )
        firestore.collection("users").document(uid).set(user).await()
    }

    suspend fun getUserData(uid: String): User? {
        return firestore.collection("users").document(uid).get().await().toObject(User::class.java)
    }

    suspend fun updateUserData(uid: String, name: String, phone: String) {
        val updates = mapOf(
            "name" to name,
            "phone" to phone
        )
        firestore.collection("users").document(uid).update(updates).await()
    }

    suspend fun updatePremiumStatus(uid: String, isPremium: Boolean) {
        firestore.collection("users").document(uid).update("isPremium", isPremium).await()
    }
}
