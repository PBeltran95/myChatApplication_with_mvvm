package ar.com.example.chatExample.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.chatExample.R
import ar.com.example.chatExample.data.models.User
import ar.com.example.chatExample.databinding.UserItemBinding
import com.bumptech.glide.Glide

class HomeAdapter (private val context:Context, private val onItemClick : OnItemClickListener) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    interface OnItemClickListener{
        fun onUserClick(user: User)
    }

    private var userList = mutableListOf<User>()

    fun setData(newList: MutableList<User>){
        val diffUtils = DiffUtils(userList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        this.userList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = userList[position]
        with(holder){
            binding.tvUserName.text = item.userName
            Glide.with(context).load(item.profileImage).into(binding.imgProfile)
            binding.root.setOnClickListener {
                onItemClick.onUserClick(item)
            }
        }
    }

    override fun getItemCount(): Int = userList.size

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = UserItemBinding.bind(view)
    }
}