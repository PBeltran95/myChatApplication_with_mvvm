package ar.com.example.chatExample.repository.messageRepo

import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.Message
import ar.com.example.chatExample.data.models.PushNotification
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface MessageRepo  {

    suspend fun getMessagesById(toId:String): Flow<Response<MutableList<Message>>>

    suspend fun sendData(messageToSend:String, toId:String )

    suspend fun sendNotifications(notification: PushNotification): retrofit2.Response<ResponseBody>
}