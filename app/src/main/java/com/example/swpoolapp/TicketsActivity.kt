package com.example.swpoolapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager

class TicketsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        val ticketsLinear = findViewById<LinearLayout>(R.id.ticketsLinear1)
        val tt = findViewById<TextView>(R.id.TicketsTV)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val frag: Fragment = TicketFragment()
        fragmentTransaction.add(ticketsLinear.id, frag)
        fragmentTransaction.commit()

    }
}