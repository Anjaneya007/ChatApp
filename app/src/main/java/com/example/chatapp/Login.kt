package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var binding:ActivityLoginBinding
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)

        //hiding action bar
        supportActionBar?.hide()

        mAuth=FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener{
            var x=Intent(this,SignUp::class.java)
            startActivity(x)
        }

        binding.btnLogIn.setOnClickListener {
            var email=binding.emailEdt.text.toString().trim()
            var password=binding.passwordEdt.text.toString().trim()

            login(email,password)
        }


    }

    private fun login(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var z=Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(z)


                } else {
                    Toast.makeText(this@Login,"User does not exist",Toast.LENGTH_SHORT).show()

                }
            }


    }
}