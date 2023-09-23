package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context,val userList:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var textName=itemView.findViewById<TextView>(R.id.txt_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View=LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser=userList[position]

        holder.textName.text=currentUser.name

        holder.itemView.setOnClickListener{
            val i= Intent(context,ChatActivity::class.java)
            i.putExtra("name",currentUser.name)
            i.putExtra("uid", currentUser.uid)
            context.startActivity(i)
        }

    }
}