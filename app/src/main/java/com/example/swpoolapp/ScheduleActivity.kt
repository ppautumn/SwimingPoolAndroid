package com.example.swpoolapp

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        CreateTable(parseTable())
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