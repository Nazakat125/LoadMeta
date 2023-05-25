package com.example.ontheroad

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ontheroad.owner.addVehicle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ownerLogin : AppCompatActivity() {
    private lateinit var phoneNumber: EditText
    private lateinit var password: EditText
    private lateinit var forgotPass:TextView
    private lateinit var logInException:TextView
    private lateinit var logInBTn: Button
    private lateinit var signUpBtn: Button
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_owner_login)
        init()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        signUpBtn.setOnClickListener {
            startActivity(Intent(this@ownerLogin,signUP::class.java))
        }
        forgotPass.setOnClickListener {
            startActivity(Intent(this@ownerLogin,forgotPassword::class.java))
        }
        logInBTn.setOnClickListener {
            val phone = phoneNumber.text.toString()
            val pass = password.text.toString()
            if (phone.isNotEmpty() && pass.isNotEmpty()) {
                if (phone.length == 11) {
                val number = "+92" + phone.substring(1)
                val db = Firebase.firestore
                progressDialog.setMessage("Please wait.....")
                progressDialog.show()
                val ref = db.collection(number).document("Profile")
                ref.get().addOnSuccessListener {
                    if (it.exists()) {
                        val P_number = it.get("Phone Number").toString()
                        val P_password = it.get("Password").toString()
                        if (number == P_number && pass == P_password) {
                            val intent = Intent(this@ownerLogin, addVehicle::class.java)
                            intent.putExtra("phone", number)
                            startActivity(intent)
                            progressDialog.dismiss()
                        } else {
                            logInException.setText("Invalid Password")
                            logInException.visibility = View.VISIBLE
                            progressDialog.dismiss()
                        }
                    } else {
                        logInException.setText("User does not exsist")
                        logInException.visibility = View.VISIBLE
                        progressDialog.dismiss()
                    }

                }.addOnFailureListener {
                    logInException.setText("$it")
                    logInException.visibility = View.VISIBLE
                    progressDialog.dismiss()
                }
            }else{
                    logInException.setText("Enter Valid Number")
                    logInException.visibility = View.VISIBLE
            }
            }else{
                if(phone.isEmpty()){
                    phoneNumber.error = "Enter Phone Number"
                }

                if (pass.isEmpty()){
                   password.error = "Enter Your Password"
                }
            }
        }
    }
    fun init(){
        phoneNumber = findViewById(R.id.loginNumber)
        password = findViewById(R.id.loginPassword)
        forgotPass = findViewById(R.id.forgotPasswordBtn)
        logInBTn = findViewById(R.id.loginBtn)
        signUpBtn = findViewById(R.id.singUpBtn)
        logInException = findViewById(R.id.logInExcetion)
    }
}