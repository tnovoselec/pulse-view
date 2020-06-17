package com.tnovoselec.android.pulseview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.ColorUtils
import kotlin.math.min

class PulseView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    companion object {
        private const val PULSE_DURATION = 1500L
        private const val REFRESH_PERIOD = 16L
    }

    private val startColor: Int = Color.GREEN
    private val endColor: Int = Color.WHITE

    private val pulsePaint = Paint()
    private var progress = 0F

    init {
        init()
    }

    private fun init() {
        pulsePaint.style = Paint.Style.FILL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startPulse()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPulse()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (measuredWidth == 0 || measuredHeight == 0) {
            return
        }

        val ratio = progress / PULSE_DURATION
        pulsePaint.color = ColorUtils.blendARGB(startColor, endColor, ratio)

        val radius = min(measuredHeight, measuredWidth) / 2 * ratio
        canvas?.drawCircle(
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(), radius.toFloat(), pulsePaint
        )
    }

    private fun startPulse() {
        handler.postDelayed(PulseRunnable(), REFRESH_PERIOD)
    }

    private fun stopPulse() {

    }

    inner class PulseRunnable : Runnable {
        override fun run() {
            progress += REFRESH_PERIOD
            progress %= PULSE_DURATION
            invalidate()
            handler.postDelayed(PulseRunnable(), REFRESH_PERIOD)
        }
    }
}