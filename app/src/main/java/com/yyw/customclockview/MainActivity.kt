package com.yyw.customclockview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var mClockView:BizClockView
    private lateinit var mClockViewHelper: ClockHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mClockView=findViewById(R.id.bcv_clock)
        mClockViewHelper= ClockHelper(mClockView)
        mClockViewHelper.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mClockViewHelper.stop()
    }
}