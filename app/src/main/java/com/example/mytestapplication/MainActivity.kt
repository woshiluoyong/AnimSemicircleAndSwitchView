package com.example.mytestapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accelerateVg = findViewById<AcceleratorAnimViewGroup>(R.id.accelerateVg)
        findViewById<View>(R.id.btn01).setOnClickListener {
            accelerateVg.setCurrentProgress(0)
        }
        findViewById<View>(R.id.btn02).setOnClickListener {
            accelerateVg.setCurrentProgress(60)
        }
        findViewById<View>(R.id.btn03).setOnClickListener {
            accelerateVg.setCurrentProgress(100)
        }

        val accelerateBtn = findViewById<AcceleratorAnimViewSwitch>(R.id.accelerateBtn)
        accelerateBtn.setOnClickListener {
            accelerateBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccBooting)
        }

        findViewById<View>(R.id.btn04).setOnClickListener {
            accelerateBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccBooting)
        }
        findViewById<View>(R.id.btn05).setOnClickListener {
            accelerateBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.Accelerating)
        }
        findViewById<View>(R.id.btn06).setOnClickListener {
            accelerateBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccStoping)
        }
        findViewById<View>(R.id.btn07).setOnClickListener {
            accelerateBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccWaiting)
        }
    }
}