package com.example.nithonline.adapter

import android.widget.ImageView
import android.widget.TextView
import com.example.nithonline.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MessageFrom(private val text:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder,position:Int){
        val ivFromRow = viewHolder.itemView.findViewById<ImageView>(R.id.iv_from_row)
        val tvFromRow = viewHolder.itemView.findViewById<TextView>(R.id.tv_from_row)

        tvFromRow.text=text
    }
    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
}
class MessageTo(private val text :String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder,position:Int){
        val ivToRow = viewHolder.itemView.findViewById<ImageView>(R.id.iv_to_row)
        val tvToRow = viewHolder.itemView.findViewById<TextView>(R.id.tv_to_row)

        tvToRow.text=text

    }
    override fun getLayout():Int{
        return R.layout.chat_to_row
    }
}