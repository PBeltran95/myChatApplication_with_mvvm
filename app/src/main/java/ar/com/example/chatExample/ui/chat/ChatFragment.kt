package ar.com.example.chatExample.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ar.com.example.chatExample.R
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.Message
import ar.com.example.chatExample.databinding.FragmentChatBinding
import ar.com.example.chatExample.presentation.auth.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var binding : FragmentChatBinding
    private val chatAdapter by lazy { ChatAdapter() }
    private val args: ChatFragmentArgs by navArgs()
    private val messageViewModel: MessageViewModel by viewModels()
    private var myUserId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        initRecyclerView()
        sendData()
        recoverMyUserId()
        fetchMessages()
    }
    private fun recoverMyUserId() {
        val fireAuth = FirebaseAuth.getInstance().currentUser?.uid
        myUserId = fireAuth!!
        chatAdapter.setIds(myUserId, args.userId)
    }

    private fun fetchMessages() {
        messageViewModel.getMessages(args.userId).observe(viewLifecycleOwner, Observer {
            when(it){
                is Response.Success -> {
                    chatAdapter.setData(it.data)
                    binding.rvMessages.smoothScrollToPosition(it.data.size)
                }
            }
        })
    }

    private fun sendData() {
        binding.btnSend.setOnClickListener {
            val messageToSend = binding.etMessage.text.toString()
            messageViewModel.sendMessage(messageToSend, args.userId)
            binding.etMessage.text.clear()
        }
    }


    private fun initRecyclerView() {
        binding.rvMessages.adapter = chatAdapter

    }
}

