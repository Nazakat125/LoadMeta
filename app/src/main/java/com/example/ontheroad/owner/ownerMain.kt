package com.example.ontheroad.owner

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.ontheroad.MainActivity
import com.example.ontheroad.R
import com.example.ontheroad.forgotPassword
import com.example.ontheroad.owner.home.ownerHome
import com.example.ontheroad.owner.khata.ownerKhata
import com.example.ontheroad.owner.map.ownerMap
import com.example.ontheroad.owner.message.ownerChat
import com.example.ontheroad.ownerLogin
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ownerMain : AppCompatActivity() {
    private lateinit var bottom_nav: BottomNavigationView
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navigationView:NavigationView
    private lateinit var progressDialog: ProgressDialog
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_main)
        phone = intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()
        progressDialog  = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait......")

        bottom_nav = findViewById(R.id.ownerButtomNavBar)
        val bundle = Bundle()
        bundle.putString("phone",phone)
        bundle.putString("vehicle",vehical)
        val home = ownerHome()
        val map = ownerMap()
        val message = ownerChat()
        val khatta = ownerKhata()



        val fragmetManager =supportFragmentManager
        val fragmentTransaction = fragmetManager.beginTransaction()
        fragmentTransaction.add(R.id.ownerFragement,home)
        home.arguments = bundle
        fragmentTransaction.commit()


        bottom_nav.setOnNavigationItemSelectedListener {
            val fragmentTransaction = fragmetManager.beginTransaction()
            if (it.itemId == R.id.owner_home){
                fragmentTransaction.replace(R.id.ownerFragement,home)
                home.arguments = bundle
            }
            else if (it.itemId == R.id.owner_map){
                fragmentTransaction.replace(R.id.ownerFragement,map)
                map.arguments = bundle
            }
            else if (it.itemId == R.id.owner_messages){
                fragmentTransaction.replace(R.id.ownerFragement,message)
                message.arguments = bundle
            }
            else if (it.itemId == R.id.owner_kahta){
                fragmentTransaction.replace(R.id.ownerFragement,khatta)
                khatta.arguments = bundle
            }
            fragmentTransaction.commit()
            true
        }

        drawerLayout = findViewById(R.id. drawerLayout)
        navigationView = findViewById(R.id.ownerNavigationView)
        val headerView = navigationView.getHeaderView(0)

        val ownerImg = headerView.findViewById<ImageView>(R.id.navigationProfileImage)
        val ownerName = headerView.findViewById<TextView>(R.id.navigationProfileName)
        val db =Firebase.firestore
        db.collection(phone).document("Profile").get().addOnSuccessListener {
            val name  = it.data?.get("Name").toString()
            val img = it.data?.get("Profile Url").toString()
            ownerName.text = name
            Glide.with(this).load(img).into(ownerImg)


        }.addOnFailureListener {
            Toast.makeText(this@ownerMain,"$it",Toast.LENGTH_LONG).show()
        }

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            val fragmentTransaction = fragmetManager.beginTransaction()
            if (it.itemId == R.id.owner_home){
                fragmentTransaction.replace(R.id.ownerFragement,home)
                home.arguments = bundle
            }
            else if (it.itemId == R.id.owner_map){
                fragmentTransaction.replace(R.id.ownerFragement,map)
                map.arguments = bundle
            }
            else if (it.itemId == R.id.owner_messages){
                fragmentTransaction.replace(R.id.ownerFragement,message)
                message.arguments = bundle
            }
            else if (it.itemId == R.id.owner_kahta){
                fragmentTransaction.replace(R.id.ownerFragement,khatta)
                khatta.arguments = bundle
            }
            else if (it.itemId == R.id.driver_cv){
                val i = Intent(this@ownerMain, driverList::class.java)
                i.putExtra("phone",phone)
                i.putExtra("vehical",vehical)
                startActivity(i)
            }
            else if (it.itemId == R.id.vehicle_reocrds){
                val r = Intent(this@ownerMain, recordList::class.java)
                r.putExtra("phone",phone)
                r.putExtra("vehical",vehical)
                startActivity(r)
            }
            else if (it.itemId == R.id.vehicle_ch_Passwords){
                val c = Intent(this@ownerMain, vehicleChangePassword::class.java)
                c.putExtra("phone",phone)
                c.putExtra("vehical",vehical)
                startActivity(c)
            }
            else if (it.itemId == R.id.delete_vehicle){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Vehicle")
                builder.setMessage("Are you sure you want to delete the vehicle?")
                builder.setPositiveButton("Delete") { _, _ ->
                    progressDialog.show()
                    val db = Firebase.firestore
                    db.collection(phone).document("Vehicles")
                        .collection("Vehicle Detail").document(vehical).delete()
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            val d = Intent(this@ownerMain,addVehicle::class.java)
                            d.putExtra("phone",phone)
                            startActivity(d)
                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(this@ownerMain,"$it",Toast.LENGTH_LONG).show()
                        }
                }

                builder.setNegativeButton("Cancel", null)
                builder.show()
            }
            else if (it.itemId == R.id.del_Account){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Account")
                builder.setMessage("Are you sure you want to delete your Account?")
                builder.setPositiveButton("Delete") { _, _ ->
                    progressDialog.show()
                    val db = Firebase.firestore
                    val db2 = FirebaseDatabase.getInstance()
                    val ref = db.collection(phone).document("Profile")
                    val refve = db.collection(phone).document("Vehicles")
                    val refRealTime = db2.getReference(phone)
                    progressDialog.show()
                    ref.delete().addOnSuccessListener {

                        refve.delete()
                        refRealTime.removeValue()
                        progressDialog.dismiss()
                        startActivity(Intent(this@ownerMain,ownerLogin::class.java))
                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@ownerMain,"$it",Toast.LENGTH_LONG)
                    }


                }
                builder.setNegativeButton("Cancel", null)
                builder.show()
            }
            else if (it.itemId == R.id.ch_password){
                val c = Intent(this@ownerMain, forgotPassword::class.java)
                startActivity(c)
            }
            else if (it.itemId == R.id.logout){
                val c = Intent(this@ownerMain, MainActivity::class.java)
                startActivity(c)
            }
            fragmentTransaction.commit()
            drawerLayout.close()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
      return super.onOptionsItemSelected(item)
    }
}
