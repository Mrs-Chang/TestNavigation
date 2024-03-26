package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.GravityCompat

class VerticalLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {
    private val availableRect = Rect()
    private var maxWidth = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var childMatchParent = false
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var totalHeight = 0
        for (i in 0..<childCount) {
            val child = getChildAt(i)
            if (widthMode != MeasureSpec.EXACTLY && child.layoutParams.width == LayoutParams.MATCH_PARENT) {
                childMatchParent = true
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val childLp = child.layoutParams as MarginLayoutParams
            Log.d(
                "VerticalLayout",
                "onMeasure: $i width = ${child.measuredWidth},lp.width:${childLp.width}, height = ${child.measuredHeight},lp.height:${childLp.height}"
            )
            val childHeight = child.measuredHeight + childLp.topMargin + childLp.bottomMargin
            totalHeight += childHeight
            maxWidth =
                maxWidth.coerceAtLeast(child.measuredWidth + childLp.leftMargin + childLp.rightMargin)
        }
        totalHeight += paddingTop + paddingBottom
        setMeasuredDimension(
            resolveSize(maxWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
        if (childMatchParent) {
            forceUniformWidth(heightMeasureSpec)
        }
    }

    private fun forceUniformWidth(heightMeasureSpec: Int) {
        val uniformWidthSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childLp = child.layoutParams
            if (childLp.width == LayoutParams.MATCH_PARENT) {
                measureChildWithMargins(child, uniformWidthSpec, 0, heightMeasureSpec, 0)
                Log.d("VerticalLayout", "forceUniformWidth: childLp:${childLp.width}")
            }
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var currentTop = paddingTop
        for (i in 0 until childCount) {
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