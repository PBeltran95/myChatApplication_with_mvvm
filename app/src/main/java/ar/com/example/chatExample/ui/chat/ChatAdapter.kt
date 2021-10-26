package ar.com.example.chatExample.ui.chat

import android.icu.text.DateFormat.HOUR0_FIELD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.chatExample.R
import ar.com.example.chatExample.data.models.Message
import ar.com.example.chatExample.databinding.MessageItemBinding
import com.google.gson.internal.bind.util.ISO8601Utils.format
import java.text.DateFormat
import java.text.DateFormat.HOUR0_FIELD
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.toDuration

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var messageList = mutableListOf<Message>()
    private var fromUserId = ""
    private var toId = ""
    fun setIds(fromUserId:String, toId:String){
        this.fromUserId = fromUserId
        this.toId = toId
    }

    fun setData(newMessages: MutableList<Message>){
        this.messageList = newMessages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(layoutInflater.inflate(R.layout.message_item, parent, false))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when(currentMessage.fromId){
            toId -> {
                holder.binding.tvMessage.apply {
                    text = currentMessage.message
                    isVisible = true
                }
                holder.binding.tvBotMessage.isVisible = false
                holder.binding.tvTime.isVisible = false
                holder.binding.tvTimeMessage.apply {
                    isVisible = true
                    text = currentMessage.created_at?.let {
                        DateFormat.getTimeInstance(DateFormat.SHORT).format(it)
                    }
                }
            }
            fromUserId -> {
                holder.binding.tvBotMessage.apply {
                    text = currentMessage.message
                    isVisible = true
                }
                holder.binding.tvTime.apply {
                    isVisible = true
                    text = currentMessage.created_at?.let {
                        DateFormat.getTimeInstance(DateFormat.SHORT).format(it)
                    }

                }
                holder.binding.tvMessage.isVisible = false
                holder.binding.tvTimeMessage.isVisible = false
            }
            else -> {holder.binding.tvMessage.isVisible = false
                     holder.binding.tvBotMessage.isVisible = false
                    holder.binding.tvTime.isVisible = false
                holder.binding.tvTimeMessage.isVisible = false}
        }
    }

    override fun getItemCount(): Int = messageList.size

    inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = MessageItemBinding.bind(view)
    }
}