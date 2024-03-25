package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import kotlin.math.abs

class ScrollBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attributeSet) {

    private var totalScroll = 0


    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        parent.onLayoutChild(child, layoutDirection)
        return true
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }


    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        //边界处理
        var cosumedy = dy
        val scroll = totalScroll + dy
        if (abs(scroll) > getMaxScroll(child)) {
            cosumedy = getMaxScroll(child) - abs(totalScroll)
        }
        else if (scroll < 0) {
            cosumedy = 0
        }
        //在这里进行事件消费，我们只需要关心竖向滑动
        ViewCompat.offsetTopAndBottom(child, -cosumedy)
        //重新赋值
        totalScroll += cosumedy
        consumed[1] = cosumedy
        Log.d("GaoChang", "onNestedPreScroll:dy:$dy, totalScroll:$totalScroll")
    }

    private fun getMaxScroll(child: View): Int {
        return child.height
    }


}