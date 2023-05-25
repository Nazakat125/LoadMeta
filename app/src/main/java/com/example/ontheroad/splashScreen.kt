package com.example.ontheroad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_splash_screen)
        val imageView = findViewById<ImageView>(R.id.splash_screen_image)
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash_screen)
            .into(imageView)


        Handler().postDelayed({
            val intent = Intent(this@splashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}