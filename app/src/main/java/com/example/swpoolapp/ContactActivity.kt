package com.example.swpoolapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        val listViews = ArrayList<TextView>()
        val tvAddr = findViewById<TextView>(R.id.AddrTV)
        val tvNum = findViewById<TextView>(R.id.NymTV)
        val tvDev = findViewById<TextView>(R.id.DevTV)
        listViews.add(tvAddr); listViews.add(tvNum); listViews.add(tvDev);

        for (tv in listViews) {
            tv.setOnClickListener {
                val text = "Скопировано: ${tv.text.split(": ")[1]}"

                val myClipboard: ClipboardManager
                myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val myClip: ClipData
                myClip = ClipData.newPlainText("text", text)
                myClipboard.setPrimaryClip(myClip)

                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
    }
}