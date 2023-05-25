package com.example.ontheroad

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class reCreatePassword : AppCompatActivity() {
    private lateinit var f_password:EditText
    private lateinit var f_RePassword:EditText
    private lateinit var f_doneBtn:Button
    private lateinit var f_exception:TextView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_re_create_password)
        init()
        val phone = intent.getStringExtra("phone").toString()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        f_doneBtn.setOnClickListener {
           val password  = f_password.text.toString()
           val Repassword  = f_RePassword.text.toString()
            if (password.length == 6 && Repassword.length== 6){
                if (password == Repassword){
                    progressDialog.setMessage("Please Wait.....")
                    progressDialog.show()
                    val db = Firebase.firestore
                    val ref = db.collection(phone).document("Profile")
                    val data = hashMapOf("Password" to password, "Re_Password" to Repassword)
                   ref.update(data as Map<String, Any>).addOnSuccessListener {
                       progressDialog.dismiss()
                       startActivity(Intent(this@reCreatePassword,ownerLogin::class.java))
                       Toast.makeText(this@reCreatePassword,"Password change Successfully",Toast.LENGTH_LONG).show()
                   }.addOnFailureListener {
                       progressDialog.dismiss()
                       f_exception.setText("Network error")
                       f_exception.visibility = View.VISIBLE
                   }
                }else{
                    f_exception.setText("Enter same Values in Both Box")
                    f_exception.visibility = View.VISIBLE
                }
            }else{
                f_exception.setText("Password must be Six digit Long")
                f_exception.visibility = View.VISIBLE
            }
        }
    }
    fun init(){
        f_password = findViewById(R.id.f_UserPassword)
        f_RePassword = findViewById(R.id.f_UserRePassword)
        f_doneBtn = findViewById(R.id.f_UserdoneBtn)
        f_exception = findViewById(R.id.f_UserException)
    }
}