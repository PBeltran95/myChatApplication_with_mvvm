package ar.com.example.chatExample.data.remote.auth

import android.graphics.Bitmap
import ar.com.example.chatExample.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authInstance: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val authResult = authInstance.signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun signUp(
        email: String,
        password: String,
        userName: String,
        userImage: Bitmap?
    ): FirebaseUser? {
        val authResult = authInstance.createUserWithEmailAndPassword(email, password).await()

        val user = authInstance.currentUser
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/profile_image")
        val baos = ByteArrayOutputStream()
        userImage?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val photoUrl = imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        authResult.user?.uid?.let {
            firebaseFirestore.collection("users").document(it).set(User(user!!.uid, email, userName, photoUrl))
        }
        return authResult.user
    }

    suspend fun getUserInfo():User?{
        val userId = authInstance.currentUser?.uid
        val queryUser = firebaseFirestore.collection("users").document(userId!!).get().await()
        return queryUser.toObject(User::class.java)
    }

    suspend fun getUsers():MutableList<User>{
        val userId = authInstance.currentUser?.uid
        val queryActualUser = firebaseFirestore.collection("users").document("$userId").get().await()
        val myUserObject = queryActualUser.toObject(User::class.java)
        val queryUser = firebaseFirestore.collection("users").get().await()
        val userList = mutableListOf<User>()
        for (user in queryUser){
            userList.add(user.toObject(User::class.java))
        }
        if (userList.contains(myUserObject)){userList.remove(myUserObject)}

        return userList
    }

    fun logOut() {
        authInstance.signOut()
    }

    fun isUserLogged():Boolean {
        return authInstance.currentUser != null
    }
}