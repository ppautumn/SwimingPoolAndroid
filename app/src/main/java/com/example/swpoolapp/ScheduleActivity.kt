package com.example.swpoolapp

import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        val currentDate = LocalDateTime.now()
        val curerntDateStr = currentDate.format(DateTimeFormatter.ofPattern("YYYY-MM-DD"))
        val lastDate = getLastDate(currentDate)
        val lastDateStr =
            lastDate.toString().subSequence(0, lastDate.toString().length - 6).toString()
        //Log.e("MyDate0", curerntDateStr)
        //Log.e("MyDate1", lastDateStr)
        var responseJSON = ""
        val x = object : Thread() {
            override fun run() {
                println("running from Thread: ${Thread.currentThread()}")
                responseJSON =
                    sendGet("http://192.168.0.105/tracks-schedule/?start=${curerntDateStr}&end=${lastDateStr}");

            }
        }
        x.start()
        x.join()
        Log.e("MyJSON", responseJSON)
        CreateTable(parseTable())
    }

    fun getLastDate(currentDate: LocalDateTime): LocalDateTime {
        var day = currentDate.dayOfMonth + 14
        var month = currentDate.month.value
        var year = currentDate.year
        if (day > currentDate.month.length(currentDate.year % 4 == 0)) {
            if (month == 11)
                year.plus(1)
            month += 1
            day %= currentDate.month.length(currentDate.year % 4 == 0)
        }
        val lastDate = LocalDateTime.of(year, month, day, 0, 0)
        return lastDate
    }


    fun CreateTable(Table: ArrayList<ArrayList<String>>) {
        val scale: Float = this.getResources().getDisplayMetrics().density

        val GUITable = findViewById<TableLayout>(R.id.TableView)
        var i = 0
        for (line in Table) {
            var j = 0
            val rowTable = TableRow(this)
            var nodes = arrayListOf<TextView>()

            for (node in line) {
                val tv = TextView(this)
                tv.text = node;
                tv.setTextColor(ContextCompat.getColor(this, R.color.black))
                tv.setTextSize(18f)
                if (i == 0)
                    tv.setTextSize(24f)
                tv.layoutParams =
                    TableRow.LayoutParams((100 * scale + 0.5f).toInt(), (50 * scale + 0.5f).toInt())
                if (i % 2 == 1)
                    tv.setBackgroundColor(ContextCompat.getColor(this, R.color.TBgray))
                rowTable.addView(tv)
                j++
            }
            i++
            GUITable.addView(rowTable)
        }
    }

    private fun sendGet(URLReq: String): String {
        println("kholod")
        val urlTxt = URLReq
        val url = URL(urlTxt)
        val urlConnection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            doInput = true
        }
        urlConnection.connect()
        val inputStream = urlConnection.inputStream
        val answ = inputStream.bufferedReader().readText()
        return answ
    }

    fun parseTable(): ArrayList<ArrayList<String>> {
        val table = arrayListOf<ArrayList<String>>()
        for (i in (1..15)) {
            val a = arrayListOf<String>()
            for (j in (1..15))
                a.add(i.toString() + " " + j.toString())
            table.add(a)
        }
        return table;
    }
}