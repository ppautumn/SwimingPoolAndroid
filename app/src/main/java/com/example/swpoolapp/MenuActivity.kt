package com.example.swpoolapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btProfile = findViewById<Button>(R.id.MenueLkBT)
        val btTickets = findViewById<Button>(R.id.MenuAbonBT)
        val btSchedule = findViewById<Button>(R.id.MenueTtBT)
        val btSeans = findViewById<Button>(R.id.MenuSeansBT)
        val btCont = findViewById<Button>(R.id.MenueKontBT)

        btCont.setOnClickListener {
            val intent = Intent(this@MenuActivity, ContactActivity::class.java)
            startActivity(intent)
        }

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

        btSeans.setOnClickListener {
            val intent = Intent(this@MenuActivity, SeanceActivity::class.java)
            startActivity(intent)
        }
    }
}