package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    lateinit var binding:ActivitySignUpBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var db:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        //hiding action bar
        supportActionBar?.hide()

        mAuth=FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            var email=binding.emailEdt.text.toString().trim()
            var password=binding.passwordEdt.text.toString().trim()
            var name=binding.nameEdt.text.toString().trim()

            signUp(name,email,password)
        }

    }

    private fun signUp(name:String,email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email, mAuth.currentUser?.uid!!)
                    val y=Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(y)

                } else {
                    Toast.makeText(this@SignUp,"some error occurred",Toast.LENGTH_SHORT).show()

                }
            }

    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        db=FirebaseDatabase.getInstance().reference
        var me=User(name,email,uid)

        db.child("user").child(uid).setValue(me)


    }
}