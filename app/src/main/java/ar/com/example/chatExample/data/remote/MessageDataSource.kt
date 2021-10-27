package ar.com.example.chatExample.data.remote

import ar.com.example.chatExample.core.FireBaseNotificationService
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.Message
import ar.com.example.chatExample.data.models.PushNotification
import com.bumptech.glide.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import java.lang.Exception
import javax.inject.Inject

class MessageDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val notificationService: FireBaseNotificationService
) {

    private val myUserId = auth.currentUser?.uid

    @ExperimentalCoroutinesApi
    suspend fun getMessagesById(toId: String): Flow<Response<MutableList<Message>>> = callbackFlow {
        val listOfMessages = mutableListOf<Message>()
        var messageReference: Query? = null

        try {
            messageReference = fireStore.collection("/messages").document("$myUserId").collection(toId)
                .orderBy("created_at", Query.Direction.ASCENDING)
        } catch (e: Throwable) {
            close(e)
        }

        val subscription = messageReference?.addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener
            listOfMessages.clear()
            try {
                val messages = value.toObjects(Message::class.java)
                for (message in messages) {
                    //This if block gets the messages from my user to my mate
                    if (message.fromId == myUserId && message.toId == toId) {
                        listOfMessages.add(message)
                    }
                    //This  if block gets the messages from my mate to me.
                    if (message.toId == myUserId && message.fromId == toId) {
                        listOfMessages.add(message)
                    }
                }
            }catch (e:Exception){
                close(e)
            }

            trySend(Response.Success(listOfMessages))
        }
        awaitClose { subscription?.remove() }
    }

    suspend fun sendData(messageToSend:String, toId:String ){
        val fromId = auth.currentUser?.uid
        fromId?.let {
            fireStore.collection("/messages").document("$myUserId").collection(toId).document().set(Message(messageToSend, it, toId = toId)).await()
            fireStore.collection("/messages").document(toId).collection("$myUserId").document().set(Message(messageToSend, it, toId = toId)).await()
        }
    }

    suspend fun sendNotifications(notification:PushNotification): retrofit2.Response<ResponseBody> = notificationService.postNotification(notification)

}