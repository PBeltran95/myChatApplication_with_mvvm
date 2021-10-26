package ar.com.example.chatExample.repository.messageRepo

import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepo  {

    suspend fun getMessagesById(toId:String): Flow<Response<MutableList<Message>>>

    suspend fun sendData(messageToSend:String, toId:String )
}