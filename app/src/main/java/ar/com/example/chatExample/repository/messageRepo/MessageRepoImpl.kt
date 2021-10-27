package ar.com.example.chatExample.repository.messageRepo

import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.Message
import ar.com.example.chatExample.data.models.PushNotification
import ar.com.example.chatExample.data.remote.MessageDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class MessageRepoImpl @Inject constructor (private val messageDataSource: MessageDataSource) : MessageRepo {


    override suspend fun getMessagesById(toId: String): Flow<Response<MutableList<Message>>> =
        messageDataSource.getMessagesById(toId)

    override suspend fun sendData(messageToSend: String,  toId: String) {
        messageDataSource.sendData(messageToSend, toId)
    }

    override suspend fun sendNotifications(notification: PushNotification): retrofit2.Response<ResponseBody> {
        return messageDataSource.sendNotifications(notification)
    }
}