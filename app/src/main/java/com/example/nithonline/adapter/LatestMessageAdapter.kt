package com.example.nithonline.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import com.example.nithonline.R
import com.example.nithonline.model.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageAdapter(private val latestMessage:Message):Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        val tvLatestMessage = viewHolder.itemView.findViewById<TextView>(R.id.tv_latest_message)
        val tvUsernameLatest = viewHolder.itemView.findViewById<TextView>(R.id.tv_username_latest)
        val ivDpLatest =viewHolder.itemView.findViewById<ImageView>(R.id.iv_dp_latest)

        tvLatestMessage.text=latestMessage.text
    }
    override fun getLayout(): Int {
        return R.layout.latest_message_item
    }
}