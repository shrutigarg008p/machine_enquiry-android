package com.machine.machine.util


import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R


object AlertUtil {

    fun ok(
        context: Context, msg: String,
    ) {
        MaterialAlertDialogBuilder(context, R.style.RoundShapeTheme)
            .setTitle(context.getString(R.string.app_name))
            .setMessage(msg).setCancelable(true)
            .setPositiveButton(
                context.getString(R.string.ok)
            ) { dialogInterface, i ->
                run {
                    dialogInterface.dismiss()
                }
            }
            .show()

    }

    fun showDialog(
        context: Context, title: String, msg: String,
        positiveBtnText: String, negativeBtnText: String?,
        positiveBtnClickListener: DialogInterface.OnClickListener,
        negativeBtnClickListener: DialogInterface.OnClickListener?
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton(positiveBtnText, positiveBtnClickListener)
        if (negativeBtnText != null)
            builder.setNegativeButton(negativeBtnText, negativeBtnClickListener)
        val alert = builder.create()
        alert.show()
        return alert
    }


    fun logout(context: Context) {
      MaterialAlertDialogBuilder(context, R.style.RoundShapeTheme)
            .setTitle(context.getString(R.string.btn_login))
            .setMessage(context.getString(R.string.logoutDescription)).setCancelable(true)
            .setPositiveButton(
                context.getString(R.string.ok)
            ) { dialogInterface, i ->
                run {
                    EventBus.logOut()
                    dialogInterface.dismiss()
                }
            }
            .setNegativeButton(
                context.getString(R.string.cancel)
            ) { dialogInterface, i ->
                run {
                    dialogInterface.dismiss()
                }
            }
            .show()
    }


}