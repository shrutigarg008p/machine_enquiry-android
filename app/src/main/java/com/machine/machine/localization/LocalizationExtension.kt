package com.nownow.android.langhelper

import android.content.Context
import com.machine.machine.localization.LocalizationUtility

fun Context.toLocalizedContext(): Context = LocalizationUtility.getLocalizedContext(this)
