package com.example.swpoolapp

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

class SeanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seance)

        val tokensStorage =
            getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val token = tokensStorage.getString("accessToken", "")

        var answ = ArrayList<String>()
        val x = object : Thread() {
            override fun run() {
                println("running from Thread: ${Thread.currentThread()}")
                val url = "${UsefullData.serverAddr}/upcoming-slots/"
                Log.e("Seance", "token" + token)
                answ = sendGet(url, token + "", true)
                Log.e("Seance", answ[0] + " " + answ[1]);
            }
        }
        x.start()
        x.join()


        val seanceLinear = findViewById<LinearLayout>(R.id.seancesLinear1)
        val itemView =
            layoutInflater.inflate(R.layout.fragment_seance, seanceLinear)

        val btCancel = itemView.findViewById<Button>(R.id.btCancel)
        btCancel.setOnClickListener {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, "Запись удалена!", duration)
            toast.show()
        }

    }

    private fun sendGet(URLReq: String, token: String, auth: Boolean): ArrayList<String> {
        val urlTxt = URLReq
        var output = ""
        val url = URL(urlTxt)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            doInput = true
        }
        if (auth)
            conn.setRequestProperty("Authorization", "Bearer  ${token}")

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