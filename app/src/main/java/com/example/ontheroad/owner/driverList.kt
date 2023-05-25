package com.example.ontheroad.owner

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.example.ontheroad.driver.CvAdapter
import com.example.ontheroad.driver.cvData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class driverList : AppCompatActivity() {

    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var list:ArrayList<cvData>
    private lateinit var driverRecyclerView: RecyclerView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_list)

        driverRecyclerView = findViewById(R.id.driverCV)
        driverRecyclerView.layoutManager = LinearLayoutManager(this)
        phone =intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please wait......")
        val db = Firebase.firestore
        list = arrayListOf()
        val ref =db.collection(phone).document("Vehicles")
            .collection("Vehicle Detail").document(vehical)
            .collection("Driver Detail")
        progressDialog.show()
       ref.get().addOnSuccessListener {
           progressDialog.dismiss()
                for (data in it.documents){
                    val user:cvData? = data.toObject(cvData::class.java)
                    if(user != null){
                        list.add(user)
                    }
                }
                val adapter = CvAdapter(list)
                driverRecyclerView.adapter = adapter
                adapter.itemClick(object :CvAdapter.itemClick{
                    override fun clickItem(position: Int, user: cvData) {
                        Toast.makeText(this@driverList, "${user.Name}", Toast.LENGTH_SHORT).show()
                        val inten = Intent(this@driverList,driverFinal::class.java)
                        inten.putExtra("driver",user.Name)
                        inten.putExtra("phone",phone)
                        inten.putExtra("vehical",vehical)
                        startActivity(inten)
                    }


                })

            }.addOnFailureListener {
           progressDialog.dismiss()
                Toast.makeText(this@driverList,"$it", Toast.LENGTH_LONG).show()
            }
    }
}