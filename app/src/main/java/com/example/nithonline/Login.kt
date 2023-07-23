package com.example.nithonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    companion object{
        private const val TAG="Login"
    }
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var btLogin :Button
    private lateinit var tvNewAccount:TextView

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        //initializing firebase authentication
        mAuth = FirebaseAuth.getInstance()

        etEmail=findViewById(R.id.et_email)
        etPassword=findViewById(R.id.et_password)
        btLogin=findViewById(R.id.bt_login)
        tvNewAccount=findViewById(R.id.tv_new_account)

        tvNewAccount.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }

        btLogin.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Enter a valid username or password!!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this,"User doesn't exist",Toast.LENGTH_SHORT).show()
                }
            }
    }
}