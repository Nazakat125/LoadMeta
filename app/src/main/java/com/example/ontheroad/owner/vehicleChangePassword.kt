package com.example.ontheroad.owner

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ontheroad.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class vehicleChangePassword : AppCompatActivity() {
    private lateinit var password: EditText
    private lateinit var btn: Button
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_change_password)

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please wait......")

        phone =intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()

        password = findViewById(R.id.ch_vehical_pass)
        btn = findViewById(R.id.vehicalPassword)
        btn.setOnClickListener {
            progressDialog.show()
            val numberP = password.text.toString().trim()
            if (numberP.length == 6) {
                val db = Firebase.firestore
                db.collection(phone).document("Vehicles")
                    .collection("Vehicle Detail").document(vehical)
                    .update("Vehicle_Password",numberP).addOnSuccessListener {
                        progressDialog.hide()
                        Toast.makeText(this, "Password Updated", Toast.LENGTH_LONG).show()
                        val c = Intent(this@vehicleChangePassword, ownerMain::class.java)
                        c.putExtra("phone",phone)
                        c.putExtra("vehical",vehical)
                        startActivity(c)

                    }.addOnFailureListener {
                        progressDialog.hide()
                        Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                    }
            }else{
                if (numberP.isEmpty()){
                    password.error = "Enter Your Password"
                }
                if (numberP.length != 6){
                    password.error = "Password must be Six digit long"
                }

            }
        }
    }
}