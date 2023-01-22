package com.example.swpoolapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

class RegistrationActivity : AppCompatActivity() {
    val usefulData = UsefullData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val emailET = findViewById<EditText>(R.id.RegLoginET)
        val pass1ET = findViewById<EditText>(R.id.RegPassET1)
        val pass2ET = findViewById<EditText>(R.id.RegPassET2)
        val fNameET = findViewById<EditText>(R.id.RegFnameET)
        val sNameET = findViewById<EditText>(R.id.RegSnameET)

        val btIn = findViewById<Button>(R.id.RegBT)
        btIn.setOnClickListener {
            val postData =
                "email=${emailET.text}&firstname=${fNameET.text}&lastname=${sNameET.text}&password=${pass1ET.text}&password2=${pass2ET.text}"
            val url = "${usefulData.serverAddr}/auth/signup/"
            var answ = 0
            var txtToPrint = "Регистрация прошла успешно!"
            if (pass1ET.text.toString() == pass2ET.text.toString()) {
                val x = object : Thread() {
                    override fun run() {
                        println("running from Thread: ${Thread.currentThread()}")
                        answ = sendPost(url, postData)
                    }
                }
                x.start()
                x.join()
                if (!(answ in 200..299)) {
                    txtToPrint =
                        "Попробуйте другой Email"
                    emailET.hint = txtToPrint
                    emailET.setHintTextColor(Color.RED)
                    emailET.text.clear()
                }

            } else
                txtToPrint = "Пароли не совпадают!" + " ${pass1ET.text} ${pass2ET.text}"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, txtToPrint, duration)
            toast.show()
            if (answ in 200..299) {
                val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }


    fun sendPost(urlStr: String, postData: String): Int {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        var output = ""
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Content-Length", postData.length.toString())

        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
        conn.connect()
        val code = conn.responseCode
        try {
            output = conn.inputStream.bufferedReader().readText()
        } catch (e: FileNotFoundException) {
            return code
        }
        return code
    }
}