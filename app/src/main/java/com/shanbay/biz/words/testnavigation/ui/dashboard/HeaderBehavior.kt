package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import kotlin.math.abs

class HeaderBehavior<V : View> : CoordinatorLayout.Behavior<V>() {
    companion object {
        const val INVALID_POINTER = -1
    }

    private var activePointerId = INVALID_POINTER
    private var isBeingDragged = false
    private var lastMotionY = 0
    private var touchSlop = -1
    private var scroller: OverScroller? = null

    private var velocityTracker: VelocityTracker? = null //速率


    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout, child: V, ev: MotionEvent
    ): Boolean {
        if (touchSlop < 0) {
            touchSlop = ViewConfiguration.get(parent.context).scaledTouchSlop
        }
        /**
         * ev.actionMasked：常用于多点触控
         * 用来判断当前触控点的y轴偏移量是否大于touchSlop？是则返回true，否则返回false
         * */
        if (ev.actionMasked == MotionEvent.ACTION_MOVE && isBeingDragged) {
            if (activePointerId == INVALID_POINTER) {
                return false
            }
            val pointerIndex = ev.findPointerIndex(activePointerId)
            if (pointerIndex == -1) {
                return false
            }
            val y = ev.getY(pointerIndex).toInt()
            val yDiff = abs(y - lastMotionY)
            if (yDiff > touchSlop) {
                lastMotionY = y
                return true
            }
        }

        //为了拿到第一个触摸点的down的y
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            activePointerId = INVALID_POINTER
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            isBeingDragged = parent.isPointInChildBounds(child, x, y)
            if (isBeingDragged) {
                lastMotionY = y
                activePointerId = ev.getPointerId(0) //拿第一个触摸点
                ensureVelocityTracker()

                scroller?.let {
                    if (!it.isFinished) {
                        it.abortAnimation()
                        return true
                    }
                }
            }
        }

        velocityTracker?.addMovement(ev)

        return false
    }


    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        var consumeUp = false
        when (ev.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val activePointIndex = ev.findPointerIndex(activePointerId)
                if (activePointIndex == -1) {
                    return false
                }
                val y = ev.getY(activePointIndex).toInt()
                val diffY = lastMotionY - y
                lastMotionY = y
                scroll(child, diffY, getMaxDragOffset(child), 0)
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val newIndex = if (ev.actionIndex == 0) 1 else 0
                activePointerId = ev.getPointerId(newIndex)
                lastMotionY = ev.getY(newIndex).toInt()
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker?.let {
                    consumeUp = true
                    it.addMovement(ev)
                    it.computeCurrentVelocity(1000)
                    val yVelocity = it.getYVelocity(activePointerId)
                    fling(parent, child, -getMaxDragOffset(child), 0, yVelocity)
                }
            }
        }
        return super.onTouchEvent(parent, child, ev)
    }

    private fun scroll(
        header: V,
        diffY: Int,
        minOffset: Int,
        maxOffset: Int
    ): Int {
        return setHeaderTopBottomOffset(header, diffY, minOffset, maxOffset)
    }

    private fun setHeaderTopBottomOffset(
        header: V,
        newOffset: Int,
        minOffset: Int,
        maxOffset: Int
    ): Int {
        var consumed = 0
        val curOffset = header.top
        var offset = newOffset
        offset = MathUtils.clamp(offset, minOffset, maxOffset)
        if (header.top != offset) {
            header.offsetTopAndBottom(offset - header.top)
            consumed = curOffset - offset
        }
        return consumed
    }

    private fun getMaxDragOffset(view: V): Int {
        return -view.height
    }

    private fun ensureVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
    }

}