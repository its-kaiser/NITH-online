package com.example.nithonline.adapter

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.nithonline.R
import com.example.nithonline.model.Message
import com.example.nithonline.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageAdapter(private val latestMessage:Message):Item<GroupieViewHolder>(){

    var user: User?= null
    companion object{
        private const val TAG="LatestMessageAdapter"
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        val tvLatestMessage = viewHolder.itemView.findViewById<TextView>(R.id.tv_latest_message)
        val tvUsernameLatest = viewHolder.itemView.findViewById<TextView>(R.id.tv_username_latest)
        val ivDpLatest =viewHolder.itemView.findViewById<ImageView>(R.id.iv_dp_latest)

        tvLatestMessage.text=latestMessage.text

        val recipientId:String = if(latestMessage.fromId==FirebaseAuth.getInstance().uid){
            latestMessage.toId
        } else{
            latestMessage.fromId
        }
        val ref=FirebaseDatabase.getInstance().getReference("/users/$recipientId")
        //retrieving user name from database
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user=snapshot.getValue(User::class.java)
                tvUsernameLatest.text=user?.userName
                Picasso.get().load(user?.imgUrl).into(ivDpLatest)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG,"error occurred")
            }

        })
    }
    override fun getLayout(): Int {
        return R.layout.latest_message_item
    }
}