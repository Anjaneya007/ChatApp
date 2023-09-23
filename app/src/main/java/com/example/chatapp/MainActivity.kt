package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userList:ArrayList<User>
    lateinit var adapter: UserAdapter
    lateinit var mAuth:FirebaseAuth
    lateinit var db:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        db=FirebaseDatabase.getInstance().reference
        mAuth=FirebaseAuth.getInstance()

        userList= ArrayList()
        adapter= UserAdapter(this,userList)

        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter

        db.child("user").addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(me in snapshot.children){
                    var currentUser=me.getValue(User::class.java)
                    if (currentUser != null) {
                        if(mAuth.currentUser?.uid != currentUser.uid){
                            userList.add(currentUser!!)
                        }

                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.logout){
            mAuth.signOut()
            val z=Intent(this@MainActivity,Login::class.java)
            startActivity(z)
            finish()
            return true
        }

        return true
    }
}