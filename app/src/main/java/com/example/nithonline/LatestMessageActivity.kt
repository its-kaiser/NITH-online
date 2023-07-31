package com.example.nithonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.nithonline.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LatestMessageActivity : AppCompatActivity() {

    companion object{
        private const val TAG="LatestMessageActivity"
        var currentUser : User?=null
    }
    private lateinit var mAuth: FirebaseAuth

    private var db = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth=FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance()

        fetchCurrentUser()
        verifyUserIsLoggedIn()
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