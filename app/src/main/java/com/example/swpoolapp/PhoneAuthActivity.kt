package com.example.swpoolapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PhoneAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)


        val btIn = findViewById<Button>(R.id.AuthBT)
        btIn.setOnClickListener {
            val intent = Intent(this@PhoneAuthActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}