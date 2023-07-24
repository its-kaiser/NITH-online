package com.example.nithonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.nithonline.adapter.NewMessageAdapter
import com.example.nithonline.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class NewMessage : AppCompatActivity() {

    companion object{
        private const val TAG="NewMessage"
        const val USER_KEY="USER_KEY"
    }
    private var db = Firebase.database
    private lateinit var rvNewMessage :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        rvNewMessage = findViewById(R.id.rv_new_message)
        db = FirebaseDatabase.getInstance()
        supportActionBar?.title="Select user"

       /* val adapter = GroupAdapter<GroupieViewHolder>()
        rvNewMessage.adapter = adapter*/

        fetchUsers()

    }

    private fun fetchUsers() {
        val ref = db.getReference("/users")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    Log.i(TAG,it.toString())
                    val user=it.getValue(User::class.java)
                    if(user!=null) {
                        adapter.add(NewMessageAdapter(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as NewMessageAdapter
                    val intent = Intent(view.context,ChatActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)

                    finish()
                }
                
                rvNewMessage.adapter=adapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"There was some database error",error.toException())
            }

        })
    }
}
