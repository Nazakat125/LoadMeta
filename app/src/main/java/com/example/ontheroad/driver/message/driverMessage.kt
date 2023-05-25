package com.example.ontheroad.driver.message

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class driverMessage : Fragment() {
    private lateinit var driver_message:EditText
    private lateinit var driver_sendBtn:ImageButton
    private lateinit var driver_chatRecy:RecyclerView
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var chatAdapter: chatAdapter
    private lateinit var list:ArrayList<chatData>
    private lateinit var progressDialog:ProgressDialog
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_driver_message, container, false)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please wait....... ")
        phone = arguments?.getString("phone").toString()
        vehical = arguments?.getString("vehicle").toString()
        init(view)
        driver_chatRecy.layoutManager = LinearLayoutManager(requireContext())
        list = ArrayList()
        chatAdapter = chatAdapter(requireContext(),list)
        driver_chatRecy.adapter = chatAdapter
progressDialog.show()
       val  db = FirebaseDatabase.getInstance()
        db.getReference(phone).child(vehical).child("Messages").child("Driver")
            .addValueEventListener(object :ValueEventListener{
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
                    Toast.makeText(requireContext(),"$error",Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }

            })




driver_sendBtn.setOnClickListener {
  val message =driver_message.text.toString()
    if (message.isNotEmpty()){
        val sendTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
        val currentTime = timeFormat.format(sendTime)
        val sendDate = hashMapOf("Message" to message ,"Time" to currentTime,"isSend" to true)
        val reciveDate = hashMapOf("Message" to message ,"Time" to currentTime,"isSend" to false)
        val db = FirebaseDatabase.getInstance()
        db.getReference(phone).child(vehical).child("Messages").child("Driver").push().setValue(sendDate).addOnSuccessListener {
            db.getReference(phone).child(vehical).child("Messages").child("Owner").push().setValue(reciveDate)
            driver_message.text.clear()
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show()
        }
    }
}


        return view
    }

fun init(view: View){
    driver_message = view.findViewById(R.id.driver_chat)
    driver_sendBtn = view.findViewById(R.id.driver_meesageBtn)
    driver_chatRecy = view.findViewById(R.id.driver_chat_recy)
}
}