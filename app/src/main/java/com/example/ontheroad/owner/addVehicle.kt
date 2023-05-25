package com.example.ontheroad.owner

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class addVehicle : AppCompatActivity() {
    private lateinit var addVehicleBtn:ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialog: Dialog
    lateinit var vehicle_no: EditText
    lateinit var vehicleException:TextView
    lateinit var vehicle_type:AutoCompleteTextView
    lateinit var vehicle_pass: EditText
    lateinit var add_btn:Button
    lateinit var cannel_btn: Button
    private lateinit var vehicleType:String
    private lateinit var vehicleNO:String
    private lateinit var phone:String
    private lateinit var vehicleList:ArrayList<vehicle_data>
    private lateinit var progressDialog: ProgressDialog
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        dialog = Dialog(this)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.vehicle_add_popup)

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait.....")

        phone = intent.getStringExtra("phone").toString()
        recyclerView()
        init()
        var vehicle_list = arrayListOf("Dumper","Truck", "Tanker","10 wheelers", "22 wheelers","24 wheelers" )
        val vehicleAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,vehicle_list)
        vehicle_type.setAdapter(vehicleAdapter)
        vehicle_type.setOnItemClickListener { parent, view, position, id ->
            vehicleType = parent.getItemAtPosition(position) as String
        }

        add_btn.setOnClickListener {
            vehicleNO = vehicle_no.text.toString().toUpperCase()
            val vehiclePassword = vehicle_pass.text.toString().toUpperCase()
                if (vehicleNO.isNotEmpty() && vehiclePassword.isNotEmpty()) {
                    if (vehiclePassword.length == 6){
                        progressDialog.show()
                        val db = Firebase.firestore
                        val ref =db.collection(phone).document("Vehicles").collection("Vehicle Detail").document(vehicleNO)
                        val data = hashMapOf("Vehicle_No" to vehicleNO, "Vehicle_Type" to vehicleType,"Vehicle_Password" to vehiclePassword)
                        ref.get().addOnSuccessListener {
                            if (it.exists()){
                                progressDialog.dismiss()
                                vehicle_no.text.clear()
                                vehicle_type.text.clear()
                                vehicle_pass.text.clear()
                                vehicleException.setText("Vehicle Already Exsist")
                                vehicleException.visibility = View.VISIBLE
                            }else{
                                ref.set(data).addOnSuccessListener {
                                    progressDialog.dismiss()
                                    vehicle_no.text.clear()
                                    vehicle_type.text.clear()
                                    vehicle_pass.text.clear()
                                    dialog.dismiss()
                                    recyclerView()
                                    Toast.makeText(this@addVehicle,"$vehicleNO added",Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    progressDialog.dismiss()
                                    vehicleException.setText("$it")
                                    vehicleException.visibility = View.VISIBLE
                                }
                            }
                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            vehicleException.setText("$it")
                            vehicleException.visibility = View.VISIBLE
                        }
                    }else{
                        vehicle_pass.error = "Password must be six Digit Long"
                    }
                } else {
                    vehicle_no.error = "Enter Vehicle No"
                    vehicle_pass.error = "Create Vehicle Pass"
                }

        }


        cannel_btn.setOnClickListener {
            dialog.dismiss()
        }








        addVehicleBtn.setOnClickListener {
            val window: Window? = dialog.getWindow()
            window?.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            dialog.show()

        }
    }

    fun recyclerView(){
        recyclerView = findViewById(R.id.addVehicleRecyClerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        vehicleList = arrayListOf()
        val db = Firebase.firestore
        progressDialog.show()
        db.collection(phone).document("Vehicles").collection("Vehicle Detail").get().addOnSuccessListener {
            if (!it.isEmpty){
                progressDialog.dismiss()
                for (data in it.documents) {
                    val user: vehicle_data? = data.toObject(vehicle_data::class.java)
                    if (user != null) {
                        vehicleList.add(user)
                    }

                }
                val adapter = vehicleAapter(vehicleList)
                recyclerView.adapter = adapter
                adapter.onItemClickLisnear(object : vehicleAapter.onItemClickListener {
                    override fun itemclick(position: Int, text: vehicle_data) {
                        val inten = Intent(this@addVehicle,ownerMain::class.java)
                        Toast.makeText(this@addVehicle,"${text.Vehicle_No}",Toast.LENGTH_LONG).show()
                        inten.putExtra("vehical",text.Vehicle_No)
                        inten.putExtra("phone",phone)
                        startActivity(inten)

                    }

                })
            }else{
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this@addVehicle, "$it", Toast.LENGTH_LONG).show()
        }
    }

    fun init(){
        addVehicleBtn = findViewById(R.id.addVehicleBtn)
        vehicle_no = dialog.findViewById(R.id.vehiclNumber)
        vehicle_type = dialog.findViewById(R.id.vehicleType)
        vehicle_pass = dialog.findViewById(R.id.vehiclePassword)
        add_btn = dialog.findViewById(R.id.addVehicleDoneBtn)
        cannel_btn = dialog.findViewById(R.id.addVehicleCancleBtn)
        vehicleException = dialog.findViewById(R.id.vehicleExcetion)

    }
}