package com.example.ontheroad.driver

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ontheroad.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class driverDetail : AppCompatActivity() {
    private lateinit var driverList:AutoCompleteTextView
    private lateinit var driverImage:ImageView
    private lateinit var driverSelectImageBtn: Button
    private lateinit var driverName:EditText
    private lateinit var driverFatherName:EditText
    private lateinit var driverPhone:EditText
    private lateinit var driverCNIC:EditText
    private lateinit var driverCity:EditText
    private lateinit var driverAddress:EditText
    private lateinit var driverAge:EditText
    private lateinit var driverExperience:EditText
    private lateinit var driverLicences:EditText
    private lateinit var driverException:TextView
    private lateinit var driverDoneBtn: Button

    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var d_person:String

    private var uri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_detail)
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please wait.....")

        phone =intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()
        init()

        val driver = listOf("Senior Driver","Junior Driver","Conductor")
        val adapt = ArrayAdapter(this@driverDetail,android.R.layout.simple_list_item_1,driver)
        driverList.setAdapter(adapt)
        driverList.setOnItemClickListener { parent, view, position, id ->

            d_person = parent.getItemAtPosition(position) as String
        }
        val gallery =registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                driverImage.setImageURI(it)
                uri = it!!
            })
        driverSelectImageBtn.setOnClickListener {
            gallery.launch("image/*")
        }

        driverDoneBtn.setOnClickListener {
            val d_name = driverName.text.toString()
            val d_f_name = driverFatherName.text.toString()
            val d_phone = driverPhone.text.toString()
            val d_cnic = driverCNIC.text.toString()
            val d_city = driverCity.text.toString()
            val d_address = driverAddress.text.toString()
            val d_age = driverAge.text.toString()
            val d_experience = driverExperience.text.toString()
            val d_licence = driverLicences.text.toString()

            if (d_name.isNotEmpty() && d_f_name.isNotEmpty() && d_phone.isNotEmpty()
                && d_cnic.isNotEmpty() && d_city.isNotEmpty() && d_address.isNotEmpty()
                && d_age.isNotEmpty() && d_experience.isNotEmpty() && d_licence.isNotEmpty() &&
                ::d_person.isInitialized){
           progressDialog.show()
                val db = Firebase.firestore
                val ref =db.collection(phone).document("Vehicles").collection("Vehicle Detail").document(vehical).collection("Driver Detail").document(d_name)
                val dbStorge = FirebaseStorage.getInstance()
                dbStorge.getReference("Driver").child(System.currentTimeMillis().toString())
                    .putFile(uri!!).addOnSuccessListener {
                        it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            val data = hashMapOf("Name" to d_name,"Father Name" to d_f_name,"Phone" to d_phone,
                                "CNIC" to d_cnic,"City" to d_city,"Address" to d_address,
                                "Age" to d_age,"Experience" to d_experience,"Licence" to d_licence,
                                "Position" to d_person,"Image" to it)
                            ref.set(data).addOnSuccessListener {
                                progressDialog.dismiss()
                                startActivity(Intent(this@driverDetail,driverMain::class.java))
                            }.addOnFailureListener {
                                progressDialog.dismiss()
                                driverException.visibility = View.VISIBLE
                                driverException.setText("$it")
                            }


                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            driverException.visibility = View.VISIBLE
                            driverException.setText("$it")
                        }

                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        driverException.visibility = View.VISIBLE
                        driverException.setText("$it")
                    }


            }else{

                if (d_name.isEmpty()){
                    driverName.error = "Enter Name"
                }
                if (d_f_name.isEmpty()){
                    driverFatherName.error = "Enter Father Name"
                }
                if (d_phone.isEmpty()){
                    driverPhone.error = "Enter Phone Number"
                }
                if (d_cnic.isEmpty()){
                    driverCNIC.error = "Enter CNIC"
                }
                if (d_city.isEmpty()){
                    driverCity.error = "Enter Your City"
                }
                if (d_address.isEmpty()){
                    driverAddress.error = "Enter Your Address"
                }
                if (d_age.isEmpty()){
                    driverAge.error = "Enter Your age"
                }
                if (d_experience.isEmpty()){
                    driverExperience.error = "Enter Your Experience"
                }
                if (d_licence.isEmpty()){
                    driverLicences.error = "Enter Your Licence Number"
                }
                if (d_age.isEmpty()){
                    driverAge.error = "Enter Your age"
                }
                if (!::d_person.isInitialized){
                   Toast.makeText(this,"Please select Driver type",Toast.LENGTH_LONG).show()
                }


            }

        }
    }
    fun init(){
        driverList = findViewById(R.id.driverList)
        driverImage = findViewById(R.id.driverImage)
        driverSelectImageBtn= findViewById(R.id.driverImageBtn)
        driverName= findViewById(R.id.driverName)
        driverFatherName= findViewById(R.id.driverFatherName)
        driverPhone= findViewById(R.id.driverPhone)
        driverCNIC= findViewById(R.id.driverCNIC)
        driverCity= findViewById(R.id.driverCity)
        driverAddress= findViewById(R.id.driverAddress)
        driverAge= findViewById(R.id.driverAge)
        driverExperience= findViewById(R.id.driverExperience)
        driverLicences= findViewById(R.id.driverLicence)
        driverDoneBtn= findViewById(R.id.driverDoneBtn)
        driverException= findViewById(R.id.driverCvException)
    }
}