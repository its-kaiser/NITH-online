package com.example.nithonline.adapter

import com.example.nithonline.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MessageFrom: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder,position:Int){

    }
    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
}
class MessageTo: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder,position:Int){

    }
    override fun getLayout():Int{
        return R.layout.chat_to_row
    }
}