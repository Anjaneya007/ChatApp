package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    lateinit var binding:ActivityChatBinding
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList:ArrayList<Message>

    lateinit var mdb:DatabaseReference
    var receiverRoom:String?=null
    var senderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        binding=DataBindingUtil.setContentView(this@ChatActivity,R.layout.activity_chat)

//        val intent= Intent()
        val name=intent.getStringExtra("name")
        val receverUid=intent.getStringExtra("uid")
        val senderUid= FirebaseAuth.getInstance().currentUser?.uid

        mdb=FirebaseDatabase.getInstance().reference

        senderRoom=receverUid+senderUid
        receiverRoom=senderUid+receverUid

        supportActionBar?.title =name

        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)
        binding.chatRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.chatRecyclerView.adapter=messageAdapter

        //adding data to recyclerview
        mdb.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(me in snapshot.children){
                        val message=me.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                     messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        //adding message to database
        binding.sendBtn.setOnClickListener{
            val message=binding.messageBox.text.toString().trim()
            val messageObject=Message(message,senderUid)

            mdb.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mdb.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }
            binding.messageBox.setText("")


        }
    }
}