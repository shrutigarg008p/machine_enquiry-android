package com.machine.machine.ui

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.google.android.libraries.places.api.Places
import com.machine.machine.R


import com.machine.machine.localization.LanguageSetting.getDefaultLanguage
import com.machine.machine.localization.LocalizationApplicationDelegate
import com.machine.machine.offline.SharedPref
import java.util.*


class MyApplication : Application() {
    companion object {
        lateinit var instance: Application
        lateinit var resourses: Resources

    }

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun onCreate() {
        super.onCreate()
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }
        instance = this

        resourses = resources
        this.applicationContext.let { SharedPref.init(it) }
    }


    override fun attachBaseContext(base: Context?) {
        if (base != null) {
            localizationDelegate.setDefaultLanguage(base, getDefaultLanguage(base))
        }
        super.attachBaseContext(base?.let { localizationDelegate.attachBaseContext(it) })

    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    fun GetResources(): Resources {
        return localizationDelegate.getResources(this, super.getResources())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        localizationDelegate.onConfigurationChanged(this)
        super.onConfigurationChanged(newConfig)
    }

}