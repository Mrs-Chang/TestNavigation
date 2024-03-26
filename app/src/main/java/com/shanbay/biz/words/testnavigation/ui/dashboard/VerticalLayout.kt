package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.view.GravityCompat

class VerticalLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {
    private val availableRect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var totalHeight = 0
        var maxWidth = 0
        for (i in 0..<childCount) {
            val child = getChildAt(i)
            //Log.d("VerticalLayout", "onMeasure: $i childLp.width = ${childLp.width}, childLp.height = ${childLp.height}")
            //measureChild(child, widthMeasureSpec, heightMeasureSpec)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            Log.d("VerticalLayout", "onMeasure: $i width = ${child.measuredWidth}, height = ${child.measuredHeight}")
            val childLp = child.layoutParams as MarginLayoutParams
            val childHeight = child.measuredHeight + childLp.topMargin + childLp.bottomMargin
            totalHeight += childHeight
            maxWidth = maxWidth.coerceAtLeast(child.measuredWidth + childLp.leftMargin + childLp.rightMargin)
        }
        // handle vertical padding
        totalHeight += paddingTop + paddingBottom
        setMeasuredDimension(
            resolveSize(maxWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var currentTop = paddingTop
        for (i in 0..<childCount) {
            availableRect.set(
                paddingLeft,
                currentTop,
                right - paddingRight,
                bottom - paddingBottom
            )
            val child = getChildAt(i)
            val childLp = child.layoutParams as MarginLayoutParams
            val childLeft = availableRect.left + childLp.leftMargin
            val childRight = childLeft + child.measuredWidth
            val childTop = currentTop + childLp.topMargin
            val childBottom = childTop + child.measuredHeight
            child.layout(childLeft, childTop, childRight, childBottom)
            currentTop += child.measuredHeight + childLp.topMargin + childLp.bottomMargin
        }
    }

    private fun resolveGravity(gravity: Int): Int {
        return if (gravity == Gravity.NO_GRAVITY) GravityCompat.START or Gravity.TOP else gravity
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }
}