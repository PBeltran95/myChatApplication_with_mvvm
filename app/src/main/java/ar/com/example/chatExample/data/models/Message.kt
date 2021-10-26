package ar.com.example.chatExample.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(val message: String = "",
                   val fromId: String = "",
                   @ServerTimestamp
                   var created_at: Date? = null,
                   val toId:String = "")
