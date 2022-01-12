package com.animSemicircle.and.switchview

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isChangeSetVerticalAcc = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accelerateVg = findViewById<AcceleratorAnimViewGroup>(R.id.accelerateVg)
        findViewById<View>(R.id.btn01).setOnClickListener {
            accelerateVg.setCurrentProgress(0)
        }
        findViewById<View>(R.id.btn02).setOnClickListener {
            accelerateVg.setCurrentProgress(600)
        }
        findViewById<View>(R.id.btn03).setOnClickListener {
            accelerateVg.setCurrentProgress(1000)
        }

        findViewById<CheckBox>(R.id.changeAccCb).setOnCheckedChangeListener { _, isChecked ->
            isChangeSetVerticalAcc = isChecked
        }

        val accelerateHorBtn = findViewById<AcceleratorAnimViewSwitch>(R.id.accelerateHorBtn)
        val accelerateVerBtn = findViewById<AcceleratorAnimVerticalViewSwitch>(R.id.accelerateVerBtn)
        findViewById<View>(R.id.btn04).setOnClickListener {
            if(isChangeSetVerticalAcc){
                accelerateVerBtn.changeAccState(AcceleratorAnimVerticalViewSwitch.AccState.AccBooting)
            }else{
                accelerateHorBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccBooting)
            }
        }
        findViewById<View>(R.id.btn05).setOnClickListener {
            if(isChangeSetVerticalAcc){
                accelerateVerBtn.changeAccState(AcceleratorAnimVerticalViewSwitch.AccState.Accelerating)
            }else{
                accelerateHorBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.Accelerating)
            }
        }
        findViewById<View>(R.id.btn06).setOnClickListener {
            if(isChangeSetVerticalAcc){
                accelerateVerBtn.changeAccState(AcceleratorAnimVerticalViewSwitch.AccState.AccStoping)
            }else{
                accelerateHorBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccStoping)
            }
        }
        findViewById<View>(R.id.btn07).setOnClickListener {
            if(isChangeSetVerticalAcc){
                accelerateVerBtn.changeAccState(AcceleratorAnimVerticalViewSwitch.AccState.AccWaiting)
            }else{
                accelerateHorBtn.changeAccState(AcceleratorAnimViewSwitch.AccState.AccWaiting)
            }
        }
    }
}