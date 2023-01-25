package com.example.swpoolapp

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

class SeanceActivity : AppCompatActivity() {
    val dateMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seance)
        setTitle("Мои сеансы")


        val seanceLinear = findViewById<LinearLayout>(R.id.seancesLinear1)

        val tokensStorage =
            getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val token = tokensStorage.getString("accessToken", "")

        var answ = ArrayList<String>()
        val x = object : Thread() {
            override fun run() {
                println("running from Thread: ${Thread.currentThread()}")
                val url = "${UsefullData.serverAddr}/upcoming-slots/"
                answ = sendGet(url, token + "", true, "GET")
                Log.e("Seance", answ[0] + " " + answ[1]);
            }
        }
        x.start()
        x.join()
        if ((answ[0].toInt() in 200..299)) {
            if (answ[0].toInt() == 204) {
                val tv = TextView(this)
                tv.text = "На данный момент предстоящих сеансов нет";
                tv.setTextColor(ContextCompat.getColor(this, R.color.button_purp))
                tv.setTextSize(18f)
                tv.gravity = Gravity.CENTER
                seanceLinear.addView(tv)
                return
            }
            var answ2 = ArrayList<String>()
            val x2 = object : Thread() {
                override fun run() {
                    println("running from Thread: ${Thread.currentThread()}")
                    val url = "${UsefullData.serverAddr}/auth/profile/"
                    answ2 = sendGet(url, token + "", true, "GET")
                    Log.e("Seance", answ2[0] + " " + answ2[1]);
                }
            }
            x2.start()
            x2.join()

            val seanceMap = parseJsonSeance(answ[1])
            val userMap = parseJsonSeance(answ2[1])
            val frag =
                layoutInflater.inflate(R.layout.fragment_seance, seanceLinear)

            val btCancel = frag.findViewById<Button>(R.id.btCancel)
            val tvDate = frag.findViewById<TextView>(R.id.timeTV)
            val tvName = frag.findViewById<TextView>(R.id.seanceNameTV)
            val typeTV = frag.findViewById<TextView>(R.id.TypeTV1)
            val personsTV = frag.findViewById<TextView>(R.id.seancecCountTV)

            tvName.text = "${userMap.get("firstname")} ${userMap.get("lastname")}"
            personsTV.text = "${seanceMap.get("visitors")} чел. "
            typeTV.text = "Свободное плавание,\n ${seanceMap.get("track")}я дорожка 45 минут"
            if (seanceMap.get("status").toString() == "awaiting payment") {
                typeTV.text = "${typeTV.text}\nОжидает оплаты"
                typeTV.setBackgroundColor(Color.YELLOW)
            }
            tvDate.text =
                getCoolDate(seanceMap.get("date").toString()) + " " + seanceMap.get("time_slot")
            btCancel.setOnClickListener {
                var answ3 = ArrayList<String>()
                val x3 = object : Thread() {
                    override fun run() {
                        println("running from Thread: ${Thread.currentThread()}")
                        val url = "${UsefullData.serverAddr}/timetable/${seanceMap.get("id")}/"
                        answ3 = sendGet(url, token + "", true, "DELETE")
                        Log.e("Seance", answ3[0] + " " + answ3[1])
                    }
                }
                x3.start()
                x3.join()
                if ((answ3[0].toInt() in 200..299)) {
                    val cancelTv1 = findViewById<TextView>(R.id.deletedTV)
                    val cancelTv2 = findViewById<TextView>(R.id.deletedTV2)
                    cancelTv1.visibility = View.VISIBLE
                    cancelTv2.visibility = View.VISIBLE
                }
            }
        }
    }

    fun getCoolDate(date: String): String {
        val dateLDT = LocalDate.parse(date)
        val coolDate = "${dateLDT.dayOfMonth} ${UsefullData.month.get(dateLDT.month.name)}\n${
            UsefullData.days.get(dateLDT.dayOfWeek.name)
        }"
        dateMap.put(coolDate, date)
        return coolDate
    }

    fun parseJsonSeance(jsonStr: String): MutableMap<String, Any?> {
        return (Parser.default().parse(StringBuilder(jsonStr)) as JsonObject).toMutableMap()
    }

    private fun sendGet(
        URLReq: String, token: String,
        auth: Boolean, method: String
    ): ArrayList<String> {
        val urlTxt = URLReq
        var output = ""
        val url = URL(urlTxt)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = method
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