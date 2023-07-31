package com.example.nithonline.adapter

import android.widget.ImageView
import android.widget.TextView
import com.example.nithonline.R
import com.example.nithonline.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MessageFrom(private val text:String,private val user:User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder,position:Int){
        val ivFromRow = viewHolder.itemView.findViewById<ImageView>(R.id.iv_from_row)
        val tvFromRow = viewHolder.itemView.findViewById<TextView>(R.id.tv_from_row)

        tvFromRow.text=text
        Picasso.get().load(user.imgUrl).into(ivFromRow)
    }
    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
}
class MessageTo(private val text :String, private val user:User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder,position:Int){
        val ivToRow = viewHolder.itemView.findViewById<ImageView>(R.id.iv_to_row)
        val tvToRow = viewHolder.itemView.findViewById<TextView>(R.id.tv_to_row)

        tvToRow.text=text
        Picasso.get().load(user.imgUrl).into(ivToRow)

    }
    override fun getLayout():Int{
        return R.layout.chat_to_row
    }
}