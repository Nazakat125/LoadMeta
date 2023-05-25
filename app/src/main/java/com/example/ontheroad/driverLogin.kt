package com.example.ontheroad

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ontheroad.driver.driverMain
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class driverLogin : AppCompatActivity() {
    private lateinit var driverPhone:EditText
    private lateinit var vehicleNumber:EditText
    private lateinit var vehiclePassword:EditText
    private lateinit var loginBtn:Button
    private lateinit var loginException:TextView
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_driver_login)
        init()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please wait.....")

        loginBtn.setOnClickListener {
            val number = driverPhone.text.toString()
            val v_number = vehicleNumber.text.toString().toUpperCase()
            val v_password = vehiclePassword.text.toString()
            if (number.isNotEmpty() && v_number.isNotEmpty() && v_password.isNotEmpty()){
                if (number.length == 11){
                    val phone = "+92" + number.substring(1)
                    val db = Firebase.firestore
                    progressDialog.show()
                    val ref = db.collection(phone).document("Vehicles").collection("Vehicle Detail").document(v_number)
                    ref.get().addOnSuccessListener {
                        progressDialog.dismiss()
                        if (it.exists()){
                            val vehiclePass = it.get("Vehicle_Password").toString()
                            if (vehiclePass == v_password){
                                    progressDialog.dismiss()
                                val intent = Intent(this@driverLogin,driverMain::class.java)
                                intent.putExtra("phone",phone)
                                intent.putExtra("vehicleNumber",v_number)
                                startActivity(intent)
                            }else{
                                progressDialog.dismiss()
                                loginException.setText("Enter Valid Password")
                                loginException.visibility = View.VISIBLE
                            }
                        }else{
                            progressDialog.dismiss()
                            loginException.setText("Enter Valid number or Vehicle Number")
                            loginException.visibility = View.VISIBLE
                        }

                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        loginException.setText("$it")
                        loginException.visibility = View.VISIBLE
                    }

                }else{
                    loginException.setText("Enter Valid number")
                    loginException.visibility = View.VISIBLE
                }
            }else{
                if (number.isEmpty()){
                    driverPhone.error ="Enter Owner Number"
                }
                if (v_number.isEmpty()){
                    vehicleNumber.error = "Enter Vehicle Number"
                }
                if (v_password.isEmpty()){
                    vehiclePassword.error = "Enter Vehicle Password"
                }
            }
        }
    }
    fun init(){
        driverPhone = findViewById(R.id.driverLoginNumber)
        vehicleNumber = findViewById(R.id.driverVehicleNumber)
        vehiclePassword = findViewById(R.id.driverLoginPassword)
        loginBtn = findViewById(R.id.driverLoginBtn)
        loginException = findViewById(R.id.driverLogInExcetion)
    }
}