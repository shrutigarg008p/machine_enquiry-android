package com.machine.machine.offline

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.machine.machine.constant.IDConst
import com.machine.machine.constant.PrefConstant
import com.machine.machine.model.UserDTO


object SharedPref {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PrefConstant.PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun clear() {
        setUser(null)
        setToken(null)
        setUserType(null)
    }

    fun setUser(userDTO: UserDTO?) {
        if (userDTO !== null) {
            val gson = Gson()
            val json = gson.toJson(userDTO)
            val prefsEditor: SharedPreferences.Editor = prefs.edit();
            with(prefsEditor) {
                prefsEditor.putString(PrefConstant.USER, json)
                prefsEditor.commit()
            }
        } else {
            val prefsEditor: SharedPreferences.Editor = prefs.edit();
            prefsEditor.putString(PrefConstant.USER, null)
            prefsEditor.apply()
        }

    }

    fun getUser(): UserDTO? {
        val gson = Gson()
        val json: String? = prefs.getString(PrefConstant.USER, null)
        if (json != null) {
            val user: UserDTO = gson.fromJson(json, UserDTO::class.java)
            return user
        }
        return null
    }


    fun getToken(): String? {
        return prefs.getString(PrefConstant.TOKEN, null)
    }

    fun setToken(token: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(PrefConstant.TOKEN, token)
            commit()
        }
    }

    fun getUserType(): String? {
        return prefs.getString(PrefConstant.USER_TYPE, null)
    }

    fun setUserType(token: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(PrefConstant.USER_TYPE, token)
            commit()
        }
    }

    fun language(lan: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(PrefConstant.LANGUAGE, lan)
            commit()
        }
    }

    fun getLanguage(): String? {
        return prefs.getString(PrefConstant.USER_TYPE, IDConst.ENGLISH_CODE)
    }

    fun read(key: String, default: String): String? {
        return prefs.getString(key, default)
    }


    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }

    fun write(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putLong(key, value)
            commit()
        }
    }

    fun getShopId(): String? {
        return prefs.getString(PrefConstant.SHOP_ID, null)
    }

    fun setShopId(shopId: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(PrefConstant.SHOP_ID, shopId)
            commit()
        }
    }

}