package com.example.swpoolapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import org.json.JSONTokener
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL


class RegistrationActivity : AppCompatActivity() {
    val usefulData = UsefullData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setTitle("Регистрация")
        val emailET = findViewById<EditText>(R.id.RegLoginET)
        val pass1ET = findViewById<EditText>(R.id.RegPassET1)
        val pass2ET = findViewById<EditText>(R.id.RegPassET2)
        val fNameET = findViewById<EditText>(R.id.RegFnameET)
        val sNameET = findViewById<EditText>(R.id.RegSnameET)

        val btIn = findViewById<Button>(R.id.RegBT)
        btIn.setOnClickListener {
            val postData =
                "email=${emailET.text}&firstname=${fNameET.text}&lastname=${sNameET.text}&password=${pass1ET.text}&password2=${pass2ET.text}"
            val url = "${UsefullData.serverAddr}/auth/signup/"
            var answ = 0
            var txtToPrint = "Регистрация прошла успешно!"
            if (pass1ET.text.toString() == pass2ET.text.toString()) {
                val x = object : Thread() {
                    override fun run() {
                        println("running from Thread: ${Thread.currentThread()}")
                        answ = sendPost(url, postData)[0].toInt()
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
                val token = signIn(emailET.text.toString(), pass1ET.text.toString())
                if (!token.contains("Ошибка")) {
                    val intent = Intent(this@RegistrationActivity, MenuActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun signIn(email: String, pass: String): String {
        val url = "${UsefullData.serverAddr}/auth/token/"
        val postData = "email=${email}&password=${pass}"
        var answ = ArrayList<String>()
        val x = object : Thread() {
            override fun run() {
                println("running from Thread: ${Thread.currentThread()}")
                answ = sendPost(url, postData)
            }
        }
        x.start()
        x.join()
        val code = answ[0].toInt()
        if (code in 200..299) {
            //Log.e("Sign", answ[1])
            setTokensIn(answ[1])
            return answ[1]
        } else {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, "Ошибка авторизации", duration)
            toast.show()
            Log.e("Sign", "Ошибка авторизации")
            return "Ошибка авторизации"
        }
    }

    fun setTokensIn(jsonStr: String) {
        var tokensStorage =
            getSharedPreferences("tokens", Context.MODE_PRIVATE)

        var tokensJSONRefr =
            kotlin.text.StringBuilder((JSONTokener(jsonStr).nextValue() as JSONObject).getString("refresh"))
        var tokensJSONAccess =
            kotlin.text.StringBuilder((JSONTokener(jsonStr).nextValue() as JSONObject).getString("access"))
        Log.e("Sign", "parsed refr ${tokensJSONRefr} parsed access ${tokensJSONAccess}")

        val editor1: SharedPreferences.Editor = tokensStorage.edit()
        editor1.putString("refrToken", tokensJSONRefr.toString())
        editor1.putString("accessToken", tokensJSONAccess.toString())
        editor1.apply()
    }

    fun sendPost(urlStr: String, postData: String): ArrayList<String> {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        var output = ""
        conn.doOutput = true
        var pd = String(postData.toByteArray(), charset("ISO-8859-1"))
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Content-Length", pd.length.toString())

        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(pd) }
        conn.connect()
        val code = conn.responseCode
        try {
            output = conn.inputStream.bufferedReader().readText()
        } catch (e: FileNotFoundException) {
            var ar = ArrayList<String>()
            ar.add(code.toString())
            return ar
        }
        var ar = ArrayList<String>()
        ar.add(code.toString())
        ar.add(output)
        return ar
    }
}