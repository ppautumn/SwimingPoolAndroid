package com.example.swpoolapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.json.JSONObject
import org.json.JSONTokener
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleActivity : AppCompatActivity() {
    var usefulData = UsefullData();
    val dateMap = mutableMapOf<String, String>()

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
                    sendGet("${usefulData.serverAddr}/tracks-schedule/?start=${curerntDateStr}&end=${lastDateStr}");

            }
        }
        x.start()
        x.join()
        Log.e("MyJSON", responseJSON)
        val table = parseTableFromJSON(responseJSON)
        CreateTable(table)
    }

    fun getLastDate(currentDate: LocalDateTime): LocalDateTime {
        var day = currentDate.dayOfMonth + 14
        var month = currentDate.month.value
        var year = currentDate.year
        if (day > currentDate.month.length(currentDate.year % 4 == 0)) {
            if (month == 11) year.plus(1)
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
                if (i == 0) tv.setTextSize(20f)
                tv.layoutParams =
                    TableRow.LayoutParams((150 * scale + 0.5f).toInt(), (75 * scale + 0.5f).toInt())
                tv.gravity = Gravity.CENTER
                if (i % 2 == 1) tv.setBackgroundColor(ContextCompat.getColor(this, R.color.TBgray))
                val date = "hour:${line[0]} day:${Table[0][j]}"
                tv.setOnClickListener {
                    val text = date
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
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

    fun parseTableFromJSON(RawStr: String): ArrayList<ArrayList<String>> {
        val listDays = ArrayList<String>()
        val listHours = ArrayList<String>()
        val listNodes = ArrayList<ArrayList<String>>()

        val table = arrayListOf<ArrayList<String>>()

        val tableMap = mutableMapOf<String, MutableMap<String, String>>()
        var tracksJSONstrBuilder =
            kotlin.text.StringBuilder((JSONTokener(RawStr).nextValue() as JSONObject).getString("available_tracks"))
        val AllObjectsMap = Parser.default().parse(tracksJSONstrBuilder) as JsonObject

        for (day in AllObjectsMap.keys) {
            listDays.add(day)
            listNodes.add(ArrayList<String>())
        }
        val hoursRaw = AllObjectsMap[listDays[0]] as JsonObject
        for (hour in hoursRaw) {
            val tmp = hour.toString().split("=")
            listHours.add(tmp[0])
        }

        var i = 0
        for (day in listDays) {

            val hoursRaw = AllObjectsMap[day] as JsonObject
            for (hour in hoursRaw) {
                val tmp = hour.toString().split("=")
                listNodes[i].add(tmp[1])
            }
            i++;
        }

        val transpListNodes = ArrayList<ArrayList<String>>()

        val listDaysCool = ArrayList<String>()
        listDaysCool.add("")
        for (day in listDays) listDaysCool.add(getCoolDate(day))
        transpListNodes.add(listDaysCool)
        for (k in 1..(listHours.count())) {
            transpListNodes.add(ArrayList<String>())
            transpListNodes[k].add(listHours[k - 1])
            var j = 0
            for (day in listNodes) {
                transpListNodes[k].add(day[k - 1] + " Дор.")
                j++
            }
        }

        Log.e("Table", listDays.toString())
        Log.e("Table", listHours.toString())
        Log.e("Table", transpListNodes.toString())
        return transpListNodes;
    }

    fun getCoolDate(date: String): String {
        val dateLDT = LocalDate.parse(date)
        val coolDate = "${dateLDT.dayOfMonth} ${dateLDT.month.name}\n${dateLDT.dayOfWeek}"
        dateMap.put(date, coolDate)
        return coolDate
    }

    fun parseTable(): ArrayList<ArrayList<String>> {
        val table = arrayListOf<ArrayList<String>>()
        for (i in (1..15)) {
            val a = arrayListOf<String>()
            for (j in (1..15)) a.add(i.toString() + " " + j.toString())
            table.add(a)
        }
        return table;
    }
}