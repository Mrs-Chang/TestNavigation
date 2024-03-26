package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HeaderScrollingViewBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attributeSet) {
    private val tempRect1 = Rect() //
    private val tempRect2 = Rect() //

    private var verticalLayoutGap = 0
    private var overlayTop = 0

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return true
    }


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        //offsetChildAsNeeded(parent, child, dependency)
        return false
    }

    @SuppressLint("RestrictedApi")
    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val childLpHeight = child.layoutParams.height
        if (childLpHeight == LayoutParams.WRAP_CONTENT ||
            childLpHeight == LayoutParams.MATCH_PARENT
        ) {
            val dependencies = parent.getDependencies(child)
            val header =
                if (dependencies.isNotEmpty()) dependencies[0] else null // todo find first eligible view
            if (header != null) {
                var availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
                if (availableHeight > 0) {
                    if (ViewCompat.getFitsSystemWindows(header)) {
                        val parentInsets = parent.lastWindowInsets
                        if (parentInsets != null) {
                            val systemBar =
                                parentInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                            availableHeight += systemBar.top + systemBar.bottom
                        }
                    }
                } else {
                    availableHeight = parent.height
                }
                var height = availableHeight + getScrollRange(header)
                val headerHeight = header.measuredHeight
                child.translationY = 0f //Y方向移动
                height -= headerHeight
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    height,
                    if (childLpHeight == LayoutParams.WRAP_CONTENT) View.MeasureSpec.AT_MOST else View.MeasureSpec.EXACTLY
                )
                parent.onMeasureChild(
                    child,
                    parentWidthMeasureSpec,
                    widthUsed,
                    heightMeasureSpec,
                    heightUsed
                )
                return true
            }
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        val dependencies = parent.getDependencies(child)
        val header = if (dependencies.isNotEmpty()) dependencies[0] else null // todo find first eligible view
        if (header != null) {
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            val available = tempRect1 //可用区域
            available.set(
                parent.paddingLeft + lp.leftMargin,
                header.bottom + lp.topMargin,
                parent.width - parent.paddingRight - lp.rightMargin,
                parent.height + header.bottom - parent.paddingBottom - lp.bottomMargin
            )
            Log.d("GaoChang3", "onLayoutChild: available:$available")
            val parentInsets = parent.lastWindowInsets
            if (parentInsets != null
                && ViewCompat.getFitsSystemWindows(parent)
                && !ViewCompat.getFitsSystemWindows(child)
            ) {
                val systemInsets = parentInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                available.left += systemInsets.left
                available.right -= systemInsets.right
            }
            val out = tempRect2
            GravityCompat.apply(
                resolveGravity(lp.gravity),
                child.measuredWidth,
                child.measuredHeight,
                available,
                out,
                layoutDirection
            )
            Log.d("GaoChang3", "onLayoutChild: out:$out")
            child.layout(out.left, out.top, out.right, out.bottom)
            verticalLayoutGap = out.top - header.bottom
        } else {
            verticalLayoutGap = 0
            return super.onLayoutChild(parent, child, layoutDirection)
        }
        return true
    }

    private fun resolveGravity(gravity: Int): Int {
        return if (gravity == Gravity.NO_GRAVITY) GravityCompat.START or Gravity.TOP else gravity
    }


    private fun getScrollRange(header: View): Int {
        return header.measuredHeight
    }

}