package com.example.nithonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.nithonline.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChatLog :RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var user :User? =null
        //check if the user class has hasExtra
        if(intent.hasExtra(NewMessage.USER_KEY)){
            //retrieving the parcelable object into user
            user=intent.getParcelableExtra(NewMessage.USER_KEY)
        }
        
        rvChatLog = findViewById(R.id.rv_chat_log)
        val adapter = GroupAdapter<GroupieViewHolder>()
        rvChatLog.adapter=adapter
    }
}