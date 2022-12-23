package com.example.swpoolapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val btIn = findViewById<Button>(R.id.RegBT)
        btIn.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, PhoneAuthActivity::class.java)
            startActivity(intent)
        }
    }
}