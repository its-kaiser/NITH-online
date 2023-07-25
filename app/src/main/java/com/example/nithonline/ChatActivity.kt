package com.example.nithonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.nithonline.adapter.MessageFrom
import com.example.nithonline.adapter.MessageTo
import com.example.nithonline.model.Message
import com.example.nithonline.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatActivity : AppCompatActivity() {

    companion object{
        private const val TAG="ChatActivity"
    }
    private lateinit var rvChatLog :RecyclerView
    private lateinit var btSend:Button
    private var db = Firebase.database
    private lateinit var etMessage :EditText
    private lateinit var mAuth: FirebaseAuth
    private var user :User? =null
    private var adapter =GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        rvChatLog = findViewById(R.id.rv_chat_log)
        btSend = findViewById(R.id.bt_send)
        db= FirebaseDatabase.getInstance()
        etMessage=findViewById(R.id.et_message)
        mAuth=FirebaseAuth.getInstance()
        rvChatLog.adapter=adapter

        settingNameInActionBar()
        listenForMessages()
        btSend.setOnClickListener{
            Log.i(TAG,"Tried sending a message")

            sendMessage()
        }
    }

    private fun listenForMessages() {
        val ref=db.getReference("/messages")

        //for real time changes
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(Message::class.java)

                if(chatMessage!=null){
                    Log.i(TAG, chatMessage?.text.toString())

                    if(chatMessage.fromId==mAuth.uid){
                        adapter.add(MessageTo(chatMessage.text))
                    }else{
                        adapter.add(MessageFrom(chatMessage.text))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun settingNameInActionBar() {
        //check if the user class has hasExtra
        if(intent.hasExtra(NewMessage.USER_KEY)){
            //retrieving the parcelable object into user
            user=intent.getParcelableExtra(NewMessage.USER_KEY)
        }
        if(user!=null){
            supportActionBar?.title= user?.userName
        }
    }

    //sending messages to firebase
    private fun sendMessage() {
        //message to be sent
        val text = etMessage.text.toString()

        //uid of the user signed in
        val fromId = mAuth.uid

        //uid of the receiver
        val toId = user?.uid
        if(fromId==null || toId==null) {
            return
        }
        val ref = db.getReference("/messages").push()
        val chatMessage = Message(ref.key!!,text,fromId,toId,System.currentTimeMillis())
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.i(TAG,"Saved message in firebase")
            }
    }
}