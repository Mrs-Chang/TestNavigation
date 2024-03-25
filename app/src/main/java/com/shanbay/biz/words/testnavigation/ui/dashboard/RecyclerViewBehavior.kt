package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<RecyclerView>(context, attributeSet) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: RecyclerView,
        dependency: View
    ): Boolean {
        return true
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: RecyclerView,
        dependency: View
    ): Boolean {
        ViewCompat.offsetTopAndBottom(child, (dependency.bottom - child.top))
        return true
    }
}