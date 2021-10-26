package ar.com.example.chatExample.repository.auth

import android.graphics.Bitmap
import ar.com.example.chatExample.data.models.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {

    suspend fun signIn(email:String, password:String) : FirebaseUser?

    suspend fun signUp(email: String, password: String, userName:String, profileImage:Bitmap?): FirebaseUser?

    suspend fun getAllUsers():MutableList<User>

    fun logOut()

    fun isUserLoggedIn():Boolean


}