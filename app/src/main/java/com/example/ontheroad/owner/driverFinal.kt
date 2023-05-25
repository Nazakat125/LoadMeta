package com.example.ontheroad.owner

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ontheroad.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class driverFinal : AppCompatActivity() {

    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var name:String
    private lateinit var driver_name: TextView
    private lateinit var driver_position: TextView
    private lateinit var driver_Father: TextView
    private lateinit var driver_phone: TextView
    private lateinit var driver_age: TextView
    private lateinit var driver_city: TextView
    private lateinit var drvier_cnic: TextView
    private lateinit var driver_address: TextView
    private lateinit var driver_experience: TextView
    private lateinit var driver_lisence: TextView
    private lateinit var driver_image: ImageView
    private lateinit var progressDialog: ProgressDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_final)

        phone =intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()
        name = intent.getStringExtra("driver").toString()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait.......")
        driver_name = findViewById(R.id.driver_na)
        driver_Father  = findViewById(R.id.driver_father)
        driver_address = findViewById(R.id.driver_address)
        driver_city = findViewById(R.id.driver_city)
        driver_age = findViewById(R.id.driver_age)
        driver_position = findViewById(R.id.driver_type)
        driver_experience = findViewById(R.id.driver_experience)
        driver_lisence = findViewById(R.id.driver_lisence)
        driver_phone = findViewById(R.id.driver_num)
        drvier_cnic = findViewById(R.id.driver_cnic)
        driver_image = findViewById(R.id.driverImg)
        val db = Firebase.firestore
        val ref =db.collection(phone).document("Vehicles")
            .collection("Vehicle Detail").document(vehical)
            .collection("Driver Detail").document(name)

        ref.get().addOnSuccessListener {
            if (it.exists()){
                val driverName = it.data?.get("Name")?.toString()
                val driverFather = it.data?.get("Father Name")?.toString()
                val driverAge = it.data?.get("Age")?.toString()
                val driverCity = it.data?.get("City")?.toString()
                val driverCnic = it.data?.get("CNIC")?.toString()
                val drivePhone = it.data?.get("Phone")?.toString()
                val driverExperience = it.data?.get("Experience")?.toString()
                val driverAddress = it.data?.get("Address")?.toString()
                val driverPosition = it.data?.get("Position")?.toString()
                val driverLisence = it.data?.get("Licence")?.toString()
                val imgLink = it.data?.get("Image")?.toString()
                driver_name.text = driverName
                driver_position.text = driverPosition
                driver_Father.text = driverFather
                driver_age.text = driverAge
                driver_phone.text = drivePhone
                driver_city.text = driverCity
                drvier_cnic.text = driverCnic
                driver_address.text = driverAddress
                driver_experience.text = driverExperience
                driver_lisence.text  = driverLisence
                Glide.with(this).load(imgLink).into(driver_image)


            }

        }.addOnFailureListener {

        }
    }
}