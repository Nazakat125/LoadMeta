package com.example.ontheroad

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var owner:Button
    private lateinit var driver:Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

        owner = findViewById(R.id.owner)
        driver = findViewById(R.id.driver)

        owner.setOnClickListener {
            startActivity(Intent(this@MainActivity,ownerLogin::class.java))
        }
        driver.setOnClickListener {
            startActivity(Intent(this@MainActivity,driverLogin::class.java))
        }
    }
}