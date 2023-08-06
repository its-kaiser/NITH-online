package com.example.nithonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.example.nithonline.adapter.LatestMessageAdapter
import com.example.nithonline.model.Message
import com.example.nithonline.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class LatestMessageActivity : AppCompatActivity() {

    companion object{
        private const val TAG="LatestMessageActivity"
        var currentUser : User?=null
    }
    private lateinit var rvLatestMessage:RecyclerView
    private var adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var mAuth: FirebaseAuth
    private var db = Firebase.database
    val latestMessageMap =HashMap<String, Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        rvLatestMessage=findViewById(R.id.rv_latest_message)
        mAuth=FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance()

        rvLatestMessage.adapter=adapter

        adapter.setOnItemClickListener{item, view->
            Log.i(TAG,"Opening a chat log")

            val userItem = item as LatestMessageAdapter

            val intent= Intent(this,ChatActivity::class.java)
            intent.putExtra(NewMessage.USER_KEY,userItem.user)
            startActivity(intent)
        }
        latestMessageListener()
        fetchCurrentUser()
        verifyUserIsLoggedIn()
    }


    private fun latestMessageListener() {
        val fromId=mAuth.uid
        val ref = db.getReference("/latest-messages/$fromId")

        //for listening new nodes
        ref.addChildEventListener(object: ChildEventListener{
            //everytime a new node(user) is added we will get notified
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val latestMessage = snapshot.getValue(Message::class.java)?: return

                latestMessageMap[snapshot.key!!]=latestMessage
                refreshRecyclerView()
            }
            //when the latest message changes it notifies us of the change
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val latestMessage =snapshot.getValue(Message::class.java) ?: return
                latestMessageMap[snapshot.key!!]=latestMessage
                refreshRecyclerView()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //recycler view will refresh everytime there is a exchange of messages
    private fun refreshRecyclerView() {
        adapter.clear()
        latestMessageMap.values.forEach{
            adapter.add(LatestMessageAdapter(it))
        }
    }

    private fun fetchCurrentUser() {
         val uid=mAuth.uid
         val ref= db.getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser=snapshot.getValue(User::class.java)
                Log.i(TAG,"Current User: ${currentUser?.userName}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mn_new_message->{
                val intent=Intent(this,NewMessage::class.java)
                startActivity(intent)
            }
            R.id.mn_signout->{
                mAuth.signOut()
                val intent=Intent(this,SignUp::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //verifying whether the user is currently logged in or not
    private fun verifyUserIsLoggedIn() {
        val uid=mAuth.uid
        if(uid==null){
            val intent=Intent(this,SignUp::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}