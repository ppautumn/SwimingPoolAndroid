package com.example.swpoolapp

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SeanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seance)

        val seanceLinear = findViewById<LinearLayout>(R.id.seancesLinear1)
        val tt = findViewById<TextView>(R.id.SeancesTV)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val frag: Fragment = seanceFragment()
        fragmentTransaction.add(seanceLinear.id, frag)
        fragmentTransaction.commit()

    }
}