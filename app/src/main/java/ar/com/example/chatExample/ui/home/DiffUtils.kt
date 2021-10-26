package ar.com.example.chatExample.ui.home

import androidx.recyclerview.widget.DiffUtil
import ar.com.example.chatExample.data.models.User

class DiffUtils(private val oldList: MutableList<User>, private val newList: MutableList<User>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userName == newList[newItemPosition].userName &&
                oldList[oldItemPosition].profileImage == newList[newItemPosition].profileImage
    }

}
