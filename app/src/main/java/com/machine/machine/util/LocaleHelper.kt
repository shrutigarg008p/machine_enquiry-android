package com.machine.machine.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.machine.machine.constant.IDConst
import com.machine.machine.offline.SharedPref
import java.util.*


object LocaleHelper {

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(Locale.getDefault().getLanguage())
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(Locale.getDefault().getLanguage())
    }

    fun setLocale(context: Context, language: String?): Context {
        persist(language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)
    }

    private fun getPersistedData(defaultLanguage: String): String? {
        return SharedPref.read(IDConst.LANGUAGE, defaultLanguage)
    }

    private fun persist(language: String?) {
        SharedPref.write(IDConst.LANGUAGE, language!!)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = context.getResources().getConfiguration()
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        return context
    }
}