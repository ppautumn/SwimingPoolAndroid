package com.example.swpoolapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReserveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)
        val arguments = intent.extras
        val date = arguments?.get("date").toString()
        val time = arguments?.get("time").toString()

        val tokensStorage =
            getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val token = tokensStorage.getString("accessToken", "")

        val trackET = findViewById<EditText>(R.id.trackET)
        val countET = findViewById<EditText>(R.id.CountET)
        val dateTV = findViewById<TextView>(R.id.DateTV)
        dateTV.text = date
        val timeTV = findViewById<TextView>(R.id.TimeTV)
        timeTV.text = time
        val btSubmit = findViewById<Button>(R.id.ReserveBT)
        btSubmit.setOnClickListener {
            var postDate =
                "date=${date}&time_slot=${time}&track=${trackET.text}&visitors=${countET.text}"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, postDate + " token: " + token, duration)
            toast.show()
        }
    }
}