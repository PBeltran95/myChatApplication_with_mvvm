package ar.com.example.chatExample.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.repository.auth.AuthRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (private val repo: AuthRepoImpl):ViewModel() {


     fun logIn(email:String, password:String)  = liveData(Dispatchers.IO){
        emit(Response.Loading())
        try {
            emit(Response.Success(repo.signIn(email, password)))
        }catch (e:Exception){
            emit(Response.Failure(e))
        }
    }


     fun signUp(email: String, password: String, userName:String, profileImage:Bitmap?) = liveData(Dispatchers.IO){
        emit(Response.Loading())
        try {
            emit(Response.Success(repo.signUp(email, password, userName, profileImage)))
        }catch (e:Exception){
            emit(Response.Failure(e))
        }
    }

    fun getAllUsers() = liveData(Dispatchers.IO){
        emit(Response.Loading())
        try {
            emit(Response.Success(repo.getAllUsers()))
        }catch (e:Exception){
            emit(Response.Failure(e))
        }
    }

    fun logOut() {
        repo.logOut()
    }

    fun isUserLoggedIn(): Boolean = repo.isUserLoggedIn()

}