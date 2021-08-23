package com.example.mytestapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textV = TextView(this)
        setContentView(textV)

        (Handler()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 1000)
    }
}