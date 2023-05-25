package com.example.ontheroad.owner.message

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.example.ontheroad.driver.message.chatAdapter
import com.example.ontheroad.driver.message.chatData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ownerChat : Fragment() {
    private lateinit var o_message: EditText
    private lateinit var o_sendBtn: ImageButton
    private lateinit var o_chatRecy: RecyclerView
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var chatAdapter: chatAdapter
    private lateinit var list:ArrayList<chatData>
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_owner_chat, container, false)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please wait....... ")
        phone = arguments?.getString("phone").toString()
        vehical = arguments?.getString("vehicle").toString()
        init(view)
        o_chatRecy.layoutManager = LinearLayoutManager(requireContext())
        list = ArrayList()
        chatAdapter = chatAdapter(requireContext(),list)
        o_chatRecy.adapter = chatAdapter

        progressDialog.show()
        val  db = FirebaseDatabase.getInstance()
        db.getReference(phone).child(vehical).child("Messages").child("Owner")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    list.clear()
                    for (messageSnapshot in snapshot.children) {
                        val message = messageSnapshot.child("Message").getValue(String::class.java)
                        val isSend = messageSnapshot.child("isSend").getValue(Boolean::class.java)
                        val time = messageSnapshot.child("Time").getValue(String::class.java)
                        list.add(chatData(message,isSend,time))
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),"$error", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }

            })



       o_sendBtn.setOnClickListener {
            val message =o_message.text.toString()
            if (message.isNotEmpty()){
                val sendTime = System.currentTimeMillis()
                val timeFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
                val currentTime = timeFormat.format(sendTime)
                val sendDate = hashMapOf("Message" to message ,"Time" to currentTime,"isSend" to true)
                val reciveDate = hashMapOf("Message" to message ,"Time" to currentTime,"isSend" to false)
                val db = FirebaseDatabase.getInstance()
                db.getReference(phone).child(vehical).child("Messages").child("Owner").push().setValue(sendDate).addOnSuccessListener {
                    db.getReference(phone).child(vehical).child("Messages").child("Driver").push().setValue(reciveDate)
                    o_message.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    fun init(view: View){
        o_message = view.findViewById(R.id.o_chat)
       o_sendBtn = view.findViewById(R.id.o_meesageBtn)
        o_chatRecy = view.findViewById(R.id.o_chat_recy)
    }


}