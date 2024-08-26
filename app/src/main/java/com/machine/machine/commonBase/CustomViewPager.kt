package com.machine.machine.commonBase

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by Amit Rawat on 2/28/2022.
 */
/*isPagingEnabled=false disable the swipe between fragment in tabview*/
class CustomViewPager(
    context: Context?,
    attrs: AttributeSet?
) :
    ViewPager(context!!, attrs) {
    var isPagingEnabled = false   //Modify this in code for switch

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(event)
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }
}