package com.example.mytestapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accelerateV = findViewById<AcceleratorAnimView>(R.id.accelerateV)
        findViewById<View>(R.id.btn01).setOnClickListener {
            accelerateV.setCurrentProgress(60)
        }
        findViewById<View>(R.id.btn02).setOnClickListener {
            accelerateV.setCurrentProgress(30)
        }
        findViewById<View>(R.id.btn03).setOnClickListener {
            accelerateV.setCurrentProgress(100)
        }
    }
}