package com.machine.machine.ui.common


import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.coroutine.TedPermission
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityPermissionBinding
import com.machine.machine.model.requestBodies.RegisterPageIntent
import com.machine.machine.ui.maps.MapLocationActivity
import com.machine.machine.util.Utils
import kotlinx.coroutines.launch


class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {

    private lateinit var register: RegisterPageIntent
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityPermissionBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        getData()
    }

    override fun setup() {

    }


    private fun getData() {
        val intent: Intent = intent
        intent.getParcelableExtra<RegisterPageIntent>(IDConst.INTENT_USER_REGISTRATION_DATA).let {
            if (it != null) {
                register = it
            } else {
                register = RegisterPageIntent()

            }
        }
    }


    fun onBackClick(view: View) {
        finish()
    }

    fun onAllowClick(view: View) {
        permissionCheck()
    }

    fun onDenyClick(view: View) {
        Toast.makeText(this, "Deny", Toast.LENGTH_LONG).show()
    }

    fun permissionCheck() {
        if (!Utils.isGpsOn(this)) {
            buildAlertMessageNoGps()
            return
        }
        lifecycleScope.launch {
            TedPermission.create()
                .setRationaleTitle(getString(R.string.app_name))
                .setDeniedTitle(getString(R.string.permission_denied))
                .setDeniedMessage(
                    getString(R.string.reject_permission_desc)
                )
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .check().let {
                    if (it.isGranted) {
                        finish()
                        openMap()
                    } else {
                        Log.e("ted", "permissionResult: ${it.deniedPermissions}")
                    }
                }
        }
    }


    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.gps_disabled))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes),
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton(getString(R.string.no),
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun openMap() {
        Intent(this, MapLocationActivity::class.java).also {
            it.putExtra(IDConst.INTENT_USER_REGISTRATION_DATA, register)
            startActivity(it)
        }
    }
}