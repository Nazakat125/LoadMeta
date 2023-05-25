package com.example.ontheroad.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.ontheroad.MainActivity
import com.example.ontheroad.R
import com.example.ontheroad.driver.home.driverHome
import com.example.ontheroad.driver.map.driverMap
import com.example.ontheroad.driver.message.driverMessage
import com.google.android.material.bottomnavigation.BottomNavigationView

class driverMain : AppCompatActivity() {
    private lateinit var bottom_nav: BottomNavigationView
    private lateinit var phone:String
    private lateinit var vehical:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_main)
        phone = intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehicleNumber").toString()

        bottom_nav = findViewById(R.id.driverButtomNavBar)
        val bundle = Bundle()
        bundle.putString("phone",phone)
        bundle.putString("vehicle",vehical)
        val home = driverHome()
        val map = driverMap()
        val message = driverMessage()

        val fragmetManager =supportFragmentManager
        val fragmentTransaction = fragmetManager.beginTransaction()
        fragmentTransaction.add(R.id.driverFragement,home)
        home.arguments = bundle
        fragmentTransaction.commit()

        bottom_nav.setOnNavigationItemSelectedListener {
            val fragmentTransaction = fragmetManager.beginTransaction()
           if (it.itemId == R.id.driver_home){
               fragmentTransaction.replace(R.id.driverFragement,home)
               home.arguments = bundle
           }
            else if (it.itemId == R.id.driver_map){
               fragmentTransaction.replace(R.id.driverFragement,map)
               map.arguments = bundle
           }
           else if (it.itemId == R.id.driver_messages){
               fragmentTransaction.replace(R.id.driverFragement,message)
               message.arguments = bundle
           }
            fragmentTransaction.commit()
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.driver_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.driver_detail){
            val intent = Intent(this@driverMain,driverDetail::class.java)
            intent.putExtra("phone",phone)
            intent.putExtra("vehical",vehical)
            startActivity(intent)
        }
        if (item.itemId == R.id.driver_logOut){
            startActivity(Intent(this@driverMain,MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}