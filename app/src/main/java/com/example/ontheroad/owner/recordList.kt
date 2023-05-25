package com.example.ontheroad.owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class recordList : AppCompatActivity() {
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var list:ArrayList<recordsData>
    private lateinit var Recordsadapter:recordsAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)


        phone =intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()
        recyclerView = findViewById(R.id.View_RecordsRecy)
        recyclerView.layoutManager = LinearLayoutManager(this)
        list = ArrayList()
        Recordsadapter = recordsAdapter(list)
        val db = FirebaseDatabase.getInstance()
        db.getReference(phone).child(vehical).child("Fair Records").addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val date = snapshot.key.toString()
                list.add(recordsData(date))

                Recordsadapter.recodsClick(object :recordsAdapter.recodsClick{
                    override fun clikRecords(position: Int, user: recordsData) {
                        Toast.makeText(this@recordList, "Clicked item ${user.records}", Toast.LENGTH_SHORT).show()
                        val inten = Intent(this@recordList,detailRecords::class.java)
                        inten.putExtra("date",user.records)
                        inten.putExtra("phone",phone)
                        inten.putExtra("vehical",vehical)
                        startActivity(inten)
                    }
                })

                recyclerView.adapter = Recordsadapter
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}