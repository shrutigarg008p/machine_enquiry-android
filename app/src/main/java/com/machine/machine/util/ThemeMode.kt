package com.machine.machine.util

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.machine.machine.R

/**
 * Created by Amit Rawat on 2/28/2022.
 */
object ThemeMode {

    private fun isUsingNightModeResources(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> false
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    /*dark theme  and white theme
    * white              black
    * */
    fun WhiteOrBlack(context: Context): Int {

        isUsingNightModeResources(context).let {
            if (it) {
                return ContextCompat.getColor(context, R.color.white)
            } else {
                return ContextCompat.getColor(context, R.color.black)
            }
        }
    }


}