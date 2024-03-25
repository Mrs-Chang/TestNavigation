package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.ViewCompat
import kotlin.math.abs

class DependedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mLastX = 0f
    private var mLastY = 0f
    private var mlSlop = 0

    init {
        mlSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    mLastX = it.x
                    mLastY = it.y
                }

                MotionEvent.ACTION_MOVE -> {
                    ViewCompat.offsetTopAndBottom(this, it.y.toInt() - mLastY.toInt())
                    ViewCompat.offsetLeftAndRight(this, it.x.toInt() - mLastX.toInt())
                }
            }
        }
        return true
    }
}