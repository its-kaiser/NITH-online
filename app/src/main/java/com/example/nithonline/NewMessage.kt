package com.example.nithonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nithonline.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewMessage : AppCompatActivity() {

    companion object{
        private const val TAG="NewMessage"
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
                rvNewMessage.adapter=adapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"There was some database error",error.toException())
            }

        })
    }
}

class NewMessageAdapter(private val user: User): Item<GroupieViewHolder>(){

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