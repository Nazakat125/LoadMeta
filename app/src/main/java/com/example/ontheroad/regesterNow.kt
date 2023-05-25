package com.example.ontheroad

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.StartElementListener
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class regesterNow : AppCompatActivity() {
    private lateinit var profileImage:ImageView
    private lateinit var profileImageBtn:Button
    private lateinit var userName:EditText
    private lateinit var userCompanyName:EditText
    private lateinit var userAddress:EditText
    private lateinit var userPassword:EditText
    private lateinit var userRePassword:EditText
    private lateinit var doneBtn:Button
    private lateinit var exception:TextView



    private var uri: Uri? = null
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_regester_now)
        init()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        val phone =intent.getStringExtra("phone").toString()
        val gallery =registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                profileImage.setImageURI(it)
                uri = it!!
            })
        profileImageBtn.setOnClickListener {
            gallery.launch("image/*")
        }
        doneBtn.setOnClickListener {

            val name = userName.text.toString()
            val companyName = userCompanyName.text.toString()
            val address = userAddress.text.toString()
            val password = userPassword.text.toString()
            val rePassword = userRePassword.text.toString()
            if (name.isNotEmpty() && companyName.isNotEmpty()
                && address.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()){
                if (password.length == 6 && rePassword.length == 6) {
                    if (uri != null && uri != Uri.EMPTY) {
                    if (password == rePassword) {
                        val dbStorge = FirebaseStorage.getInstance()
                        progressDialog.setMessage("Uploading image.....")
                        progressDialog.show()

                        dbStorge.getReference("Owner").child(System.currentTimeMillis().toString())
                            .putFile(uri!!).addOnSuccessListener {
                            it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                                val db = Firebase.firestore
                                val ref = db.collection(phone).document("Profile")
                                val data = hashMapOf(
                                    "Name" to name,
                                    "Company Name" to companyName,
                                    "Address" to address,
                                    "Password" to password,
                                    "Re_Password" to rePassword,
                                    "Phone Number" to phone,
                                    "Profile Url" to it
                                )
                                progressDialog.setMessage("Please Wait.....")
                                ref.set(data).addOnSuccessListener {
                                    progressDialog.dismiss()
                                    startActivity(Intent(this@regesterNow, ownerLogin::class.java))
                                    Toast.makeText(
                                        this@regesterNow,
                                        "Welacome $name",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }.addOnFailureListener {
                                    progressDialog.dismiss()
                                    exception.visibility = View.VISIBLE
                                    exception.setText("$it")
                                }
                            }

                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            exception.visibility = View.VISIBLE
                            exception.setText("$it")
                        }


                    } else {
                        exception.visibility = View.VISIBLE
                        exception.setText("write the same Password in both Box")
                    }
                    }else{
                        exception.visibility = View.VISIBLE
                        exception.setText("Select your Img")
                    }

                }else{
                    exception.visibility = View.VISIBLE
                    exception.setText("Password must be Six ditgit Long")
                }
            }else{
                if (name.isEmpty()){
                    userName.error ="Enter Name"
                }
                if (companyName.isEmpty()){
                    userCompanyName.error ="Enter Company Name"
                }
                if (address.isEmpty()){
                    userAddress.error ="Enter Your Address"
                }
                if (password.isEmpty()) {
                    userPassword.error = "Make your Password"
                }
                if (rePassword.isEmpty()){
                    userRePassword.error ="Re Enter Your Password"
                }
                exception.visibility = View.VISIBLE
                exception.setText("Please fill all the Box")
            }
        }
    }
    fun init(){
        profileImage = findViewById(R.id.profileImage)
        profileImageBtn = findViewById(R.id.profileImageBtn)
        userName = findViewById(R.id.userName)
        userCompanyName = findViewById(R.id.userCompanyName)
        userAddress = findViewById(R.id.userAddress)
        userPassword = findViewById(R.id.userPassword)
        userRePassword = findViewById(R.id.userRePassword)
        doneBtn = findViewById(R.id.doneBtn)
        exception = findViewById(R.id.joinUsException)
    }
}