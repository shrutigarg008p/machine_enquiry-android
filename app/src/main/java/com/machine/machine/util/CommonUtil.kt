package com.machine.machine.util

/**
 * Created by Amit Rawat on 2/24/2022.
 */
import android.app.Activity
import android.app.Dialog
import androidx.fragment.app.Fragment


object CommonUtil {
    fun isNullOrEmpty(input: List<*>?): Boolean {
        return input == null || input.isEmpty()
    }

    fun isUIThreadRunning(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing
    }

    fun isUIThreadRunning(fragment: Fragment?, activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && fragment != null && fragment
            .isAdded
    }

    fun isNullOrEmpty(input: String?): Boolean {
        return input == null || input.isEmpty() || input.equals("null", ignoreCase = true)
    }

    fun dismissDialogSafely(activity: Activity, dialog: Dialog?) {
        if (!activity.isFinishing && dialog != null) {
            dialog.dismiss()
        }
    }


    fun showDialogSafely(activity: Activity, dialog: Dialog?) {
        if (isUIThreadRunning(activity) && dialog != null) {
            dialog.show()
        }
        if (!activity.isFinishing && dialog != null) {
            dialog.show()
        }
    }


}
