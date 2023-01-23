package com.example.swpoolapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

class ReserveActivity : AppCompatActivity() {
    val usefulData = UsefullData()
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
            var postData =
                "{\"date\": \"${date}\",\"time_slot\": \"${time}\",\"track\": ${trackET.text},\"visitors\": ${countET.text}}"

            var answ = ArrayList<String>()
            val x = object : Thread() {
                override fun run() {
                    println("running from Thread: ${Thread.currentThread()}")
                    val url = "${usefulData.serverAddr}/timetable/"
                    Log.e("Reserve", "token" + token)
                    Log.e("Reserve", "postData " + postData)
                    answ = sendPost(url, postData, token + "")
                    Log.e("Reserve", answ[0] + " " + answ[1]);
                }
            }
            x.start()
            x.join()
            Log.e("Reserve", answ[0] + " " + answ[1]);
            var toastTxt = "Удачно!"
            if (!(answ[0].toInt() in 200..299)) {
                toastTxt = "Ошибка! попробуйте отменить текущую запись."
            } else {
                val intent = Intent(this@ReserveActivity, ScheduleActivity::class.java)
                startActivity(intent)
            }
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, toastTxt, duration)
            toast.show()
        }
    }

    fun sendPost(urlStr: String, postData: String, token: String): ArrayList<String> {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        var output = ""
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Content-Length", postData.length.toString())
        conn.setRequestProperty("Authorization", "Bearer  ${token}")

        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
        Log.e("Reserve", "a");
        conn.connect()
        val code = conn.responseCode
        try {
            output = conn.inputStream.bufferedReader().readText()
        } catch (e: FileNotFoundException) {
            var ar = ArrayList<String>()
            ar.add(code.toString())
            val errTxt = conn.errorStream.bufferedReader().readText()
            ar.add(errTxt)
            return ar
        }
        var ar = ArrayList<String>()
        ar.add(code.toString())
        ar.add(output)
        return ar
    }
}