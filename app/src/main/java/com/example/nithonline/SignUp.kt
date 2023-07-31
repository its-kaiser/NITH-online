package com.example.nithonline

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.nithonline.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class SignUp : AppCompatActivity() {

    companion object {
        private const val TAG = "SignUp"
        private const val PICK_PHOTO_CODE=225
    }

    private lateinit var ivProfilePic :ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btSignup: Button
    private lateinit var tvHaveAnAccount: TextView
    private lateinit var btDp: Button
    private lateinit var mAuth: FirebaseAuth
    private val storage = Firebase.storage
    private var db = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance()
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btSignup = findViewById(R.id.bt_signup)
        tvHaveAnAccount = findViewById(R.id.have_an_account)
        btDp=findViewById(R.id.bt_dp)
        ivProfilePic=findViewById(R.id.iv_profile_pic)
        var selectedPhotoUri: Uri? =null

        btSignup.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            signUp(email, password,selectedPhotoUri)
        }
        tvHaveAnAccount.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        val getImage=registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                selectedPhotoUri=it
                val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
                ivProfilePic.setImageBitmap(bitmap)
                btDp.alpha=0f

            })
        btDp.setOnClickListener {
            Log.i(TAG, "Choose profile photo")
            getImage.launch("image/*")
        }
    }


    //new account signUp
    private fun signUp(email: String, password: String, selectedPhotoUri: Uri?) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    uploadImageToFirebaseStorage(selectedPhotoUri)

                } else {
                    // If sign up fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Error occurred while signing up!!", Toast.LENGTH_SHORT)
                        .show()

                }
            }

    }

    private fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri?) {
        if(selectedPhotoUri==null){
            return
        }
        val filePath = UUID.randomUUID().toString()
        val ref = storage.reference.child("/images/$filePath")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener { task ->
                Log.i(TAG,"Successfully uploaded image ${task.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString())
                }
            }.addOnFailureListener{
                Log.e(TAG,"error while storing data",it)
            }
    }

    private fun saveUserToDatabase(imgUrl: String) {
        val uid = mAuth.uid?: ""
        val ref = db.getReference("/users/$uid")
        val user=User(uid,etName.text.toString(),imgUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.i(TAG,"User Info Uploaded to database")

                val intent = Intent(this,LatestMessageActivity::class.java)
                //clearing the activities in the stack
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener{
                Log.e(TAG,"error while uploading data",it)
            }
    }
}
