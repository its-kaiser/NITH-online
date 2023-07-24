package com.example.nithonline.adapter

import android.widget.ImageView
import android.widget.TextView
import com.example.nithonline.R
import com.example.nithonline.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewMessageAdapter(val user: User): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val tvNewUsername = viewHolder.itemView.findViewById<TextView>(R.id.tv_new_username)
        val ivNewDp = viewHolder.itemView.findViewById<ImageView>(R.id.iv_new_dp)

        tvNewUsername.text= user.userName
        Picasso.get().load(user.imgUrl).into(ivNewDp)
    }

    override fun getLayout(): Int {
        return R.layout.new_message_item
    }
}