package com.example.electrofix.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val phone: String = ""
)

@Singleton
class ProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val usersCollection = firestore.collection("users")

    fun getUserProfileFlow(): Flow<UserProfile?> {
        val uid = auth.currentUser?.uid ?: return flowOf(null)
        return usersCollection.document(uid)
            .snapshots()
            .map { it.toObject<UserProfile>() }
    }
    
    suspend fun getUserProfile(): UserProfile? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val document = usersCollection.document(uid).get().await()
            document.toObject<UserProfile>()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUserProfile(name: String) {
        val user = auth.currentUser ?: return
        val finalName = name.ifBlank { "EFUser" }
        val userProfile = UserProfile(
            uid = user.uid,
            name = finalName,
            email = user.email ?: "",
            bio = "Welcome to ElectroFix!",
            phone = ""
        )
        usersCollection.document(user.uid).set(userProfile).await()
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        val uid = auth.currentUser?.uid ?: return
        usersCollection.document(uid).set(userProfile, SetOptions.merge()).await()
    }
}
