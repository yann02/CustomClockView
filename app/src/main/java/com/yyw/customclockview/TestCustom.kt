package com.yyw.customclockview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TestCustom(context: Context, attrs: AttributeSet) :View(context, attrs) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(0f,0f,100f, Paint())
    }
}