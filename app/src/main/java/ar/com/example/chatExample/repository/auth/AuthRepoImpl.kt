package ar.com.example.chatExample.repository.auth

import android.graphics.Bitmap
import ar.com.example.chatExample.data.models.User
import ar.com.example.chatExample.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepoImpl @Inject constructor (private val dataSource: AuthDataSource)  : AuthRepo {
    override suspend fun signIn(email: String, password: String): FirebaseUser? = dataSource.signIn(email, password)

    override suspend fun signUp(
        email: String,
        password: String,
        userName: String,
        profileImage: Bitmap?
    ): FirebaseUser? {
        return dataSource.signUp(email,password,userName, profileImage)
    }

    override suspend fun getAllUsers(): MutableList<User> = dataSource.getUsers()

    override fun logOut() {
            dataSource.logOut()
    }

    override fun isUserLoggedIn(): Boolean = dataSource.isUserLogged()
}