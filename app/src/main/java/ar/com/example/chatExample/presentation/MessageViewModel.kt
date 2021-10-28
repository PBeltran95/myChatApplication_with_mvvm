package ar.com.example.chatExample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.PushNotification
import ar.com.example.chatExample.repository.messageRepo.MessageRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(private val messageRepo: MessageRepoImpl) : ViewModel() {

    fun getMessages(toId: String) = liveData(Dispatchers.IO) {
        emit(Response.Loading())

        kotlin.runCatching {
            messageRepo.getMessagesById(toId)
        }.onSuccess { flowList ->
            flowList.collect {
                emit(it)
            }
        }.onFailure {
            emit(Response.Failure(Exception(it.message)))
        }
    }

    fun sendMessage(messageToSend: String, toId: String){
        viewModelScope.launch {
            messageRepo.sendData(messageToSend, toId)
        }
    }

    fun sendNotification(notification:PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            messageRepo.sendNotifications(notification)
        }catch (e:Exception){

        }
    }

}