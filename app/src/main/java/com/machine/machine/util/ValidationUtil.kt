package com.machine.machine.util

import android.content.Context
import android.util.Log
import android.util.Patterns
import com.machine.machine.R

object ValidationUtil {
    fun isNumeric(str: String): Boolean {
        return try {
            str.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun checkEmailorPhone(context: Context, value: String): Boolean {
        if (value.isEmpty()) {
            Log.i("check","value blank")
            AlertUtil.ok(context, Utils.getString(R.string.enter_email_or_phone))
            return false
        }
        Log.d("check", isNumeric(value).toString())
        if (!isNumeric(value)) {
            Log.i("check","value numeric")
            if (!isValidMail(value)) {
                Log.i("check","value email")
                AlertUtil.ok(context, context.getString(R.string.enter_valid_email))
                return false
            }
            return true
        } else {
            if (!checkPhoneLength(context, value)) {
                return false
            }
            return true
        }

        return true

    }

    fun isValidMail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkPassword(context: Context?, password: String?, rePassword: String?): Boolean {
        if (context != null) {
            if (password != null && password.isEmpty()) {
                AlertUtil.ok(context, context.getString(R.string.please_enter_password))
                return false
            } else if (rePassword != null && rePassword.isEmpty()) {
                AlertUtil.ok(context, context.getString(R.string.please_enter_confirm_password))
                return false
            } else if (password != rePassword) {
                AlertUtil.ok(context, context.getString(R.string.password_mismatch))
                return false
            }
        }

        return true


    }

    fun checkPhoneLength(context: Context, phone: String?): Boolean {
     /*   if (phone != null && phone.isNotEmpty()) {
            if (phone.length < 8) {
            //    AlertUtil.ok(context, context.getString(R.string.phone_number_length))
                return false
            } else if (phone.length > 12) {
              //  AlertUtil.ok(context, context.getString(R.string.phone_number_length))
                return false
            }
        }*/
        return true
    }

}