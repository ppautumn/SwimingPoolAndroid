package com.example.swpoolapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btProfile = findViewById<Button>(R.id.MenueLkBT)
        val btTickets = findViewById<Button>(R.id.MenuAbonBT)
        val btSchedule = findViewById<Button>(R.id.MenueTtBT)

        btProfile.setOnClickListener {
            val intent = Intent(this@MenuActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        btTickets.setOnClickListener {
            val intent = Intent(this@MenuActivity, TicketsActivity::class.java)
            startActivity(intent)
        }

        btSchedule.setOnClickListener {
            val intent = Intent(this@MenuActivity, ScheduleActivity::class.java)
            startActivity(intent)
        }
    }
}