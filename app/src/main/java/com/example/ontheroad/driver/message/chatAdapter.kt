package com.example.ontheroad.driver.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R

class chatAdapter(val context: Context,val chatList:ArrayList<chatData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val sendMessage = 1
    val reciveMessage = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == sendMessage){
            senderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.send_message_recy,parent,false)
            )
        }else{
            reciveViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.recive_message_recy,parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatList[position]
        if (holder.itemViewType == sendMessage){
            val viewHoder = holder as senderViewHolder
            viewHoder.sendMessage.text = message.message
            viewHoder.sendTime.text = message.time
        }else{
            val viewHoder = holder as reciveViewHolder
            viewHoder.reciveMessage.text = message.message
            viewHoder.reciveTime.text = message.time
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = chatList[position]
        return if (currentMessage.isSend == true){
            sendMessage
        }else{
            reciveMessage
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class senderViewHolder(view: View):RecyclerView.ViewHolder(view){
        val sendMessage:TextView = view.findViewById(R.id.sendMessageText)
        val sendTime:TextView = view.findViewById(R.id.sendMessageTime)
    }
    inner class reciveViewHolder(view: View):RecyclerView.ViewHolder(view){
        val reciveMessage:TextView = view.findViewById(R.id.reciveMessageText)
        val reciveTime:TextView = view.findViewById(R.id.reciveMessageTime)
    }
}