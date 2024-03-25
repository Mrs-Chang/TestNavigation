package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.shanbay.biz.words.testnavigation.R

class BrotherFollowBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<TextView>(context, attrs) {
    private lateinit var mDependedView: DependedView
    private lateinit var targetView: TextView

    override fun layoutDependsOn(
        parent: CoordinatorLayout, child: TextView, dependency: View
    ): Boolean {
        when (dependency.id) {
//            R.id.depended_view -> {
//
//
//                mDependedView = dependency as DependedView
//                targetView = child
//            }

            else -> {
                return false
            }
        }
        return true
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: TextView, dependency: View
    ): Boolean {
//        child.x = dependency.x
//        child.y = dependency.bottom.toFloat()
        //child.animate().setDuration(1000).translationY(-dependency.height.toFloat())
        return true
    }

//    override fun onLayoutChild(
//        parent: CoordinatorLayout,
//        child: TextView,
//        layoutDirection: Int
//    ): Boolean {
//        val dependencyLayoutParams = mDependedView.layoutParams as CoordinatorLayout.LayoutParams
//        val childLayoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
//        childLayoutParams.topMargin = dependencyLayoutParams.topMargin + mDependedView.height
//        child.layoutParams = childLayoutParams
//        return super.onLayoutChild(parent, child, layoutDirection)
//    }
}