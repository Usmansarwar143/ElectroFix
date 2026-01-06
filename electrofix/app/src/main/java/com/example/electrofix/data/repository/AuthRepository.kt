package com.example.electrofix.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun signUp(email: String, password: String): AuthResult = auth.createUserWithEmailAndPassword(email, password).await()

    suspend fun login(email: String, password: String): AuthResult = auth.signInWithEmailAndPassword(email, password).await()

    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun signOut() {
        auth.signOut()
    }
}