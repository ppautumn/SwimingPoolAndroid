package com.example.swpoolapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import org.json.JSONTokener
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    val usefulData = UsefullData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btIn = findViewById<Button>(R.id.InpBT)
        val tvForgetPass = findViewById<TextView>(R.id.ForgetPassTV)
        val tvNoReg = findViewById<TextView>(R.id.noAuthTB)
        val etLogin = findViewById<EditText>(R.id.LoginET)
        val etPass = findViewById<EditText>(R.id.PassET)
        val ipET = findViewById<EditText>(R.id.ipET1)
        val ipBT = findViewById<Button>(R.id.IpBT)
        ipBT.setOnClickListener {
            UsefullData.serverAddr = ipET.text.toString()
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, UsefullData.serverAddr, duration)
            toast.show()
        }


        btIn.setOnClickListener {
            val token = signIn(etLogin.text.toString(), etPass.text.toString())
            if (!token.contains("Ошибка")) {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivity(intent)
            } else {
                etPass.text.clear()
                etPass.hint = "Неверный логин или пароль"
                etPass.setHintTextColor(Color.RED)
            }
        }

/*        tvForgetPass.setOnClickListener {
            val intent = Intent(this@MainActivity, MenuActivity::class.java)
            startActivity(intent)
        }*/

        tvNoReg.setOnClickListener {
            val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(intent)
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
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Content-Length", postData.length.toString())

        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
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