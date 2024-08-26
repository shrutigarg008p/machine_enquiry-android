package com.machine.machine.util


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.gun0912.tedpermission.coroutine.TedPermission
import com.machine.machine.R
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.model.UserDTO
import com.machine.machine.model.requestBodies.RegisterPageIntent
import com.machine.machine.model.response.DataDto
import com.machine.machine.model.response.seller.ProductsDto
import com.machine.machine.network.RetrofitInstance
import com.machine.machine.offline.SharedPref
import com.machine.machine.ui.MyApplication
import com.machine.machine.ui.common.LoginActivity
import com.machine.machine.ui.common.PermissionActivity
import com.machine.machine.ui.common.RegistrationActivity
import com.machine.machine.ui.seller.SellerMainActivity
import com.machine.machine.ui.seller.screen.ProductDetailActivity
import com.machine.machine.ui.user.UMainActivity
import kotlinx.coroutines.launch
import java.net.URLEncoder


object Utils {
    fun clearAllData() {
        SharedPref.clear()
        RetrofitInstance.clean()
    }

    fun isTypeCustomer(type: String?): Boolean {
        if (type != null && type == IDConst.CUSTOMER) {
            return true
        }
        return false
    }

    fun getLogin(context: Context) {
        SharedPref.clear()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    fun getHome(context: Context) {
        val intent = Intent(context, UMainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }


    fun getSellerHome(context: Context) {
        val intent = Intent(context, SellerMainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    fun getProductDetail(context: Context,productsDto: ProductsDto) {
        val intent = Intent(context, ProductDetailActivity::class.java)
        if (productsDto != null) {
            intent.putExtra(IDConst.INTENT_PRODUCT_DETAIL_DATA, productsDto)
        }
        context.startActivity(intent)
    }

    fun getRegistration(
        context: Context,
        register: RegisterPageIntent?
    ) {

        val intent = Intent(context, RegistrationActivity::class.java)
        if (register != null) {
            intent.putExtra(IDConst.INTENT_USER_REGISTRATION_DATA, register)

        }
        context.startActivity(intent)
    }

    fun getPermissionActivity(context: Context, register: RegisterPageIntent?) {

        val intent = Intent(context, PermissionActivity::class.java)
        if (register != null) {
            intent.putExtra(IDConst.INTENT_USER_REGISTRATION_DATA, register)

        }
        context.startActivity(intent)
    }

    fun savedRegistrationData(user: UserDTO?, context: Context) {
        if (user != null) {

            /*if onboared:true registration completed go to home */
            if (user.onboarded != null && user.onboarded == true) {
                SharedPref.setUser(user)
                SharedPref.setUserType(user.type)
                 if(user.type.equals("customer"))
                 {
                     getHome(context)
                 }
                else
                 {
                     getSellerHome(context)
                 }


            } else {
                AlertUtil.ok(
                    context = context,
                    msg = context.getString(R.string.something_went_wrong)
                )
            }
        } else {
            AlertUtil.ok(
                context = context,
                msg = context.getString(R.string.data_is_empty)
            )

        }
    }

    fun savedData(dataDto: DataDto?, context: Context) {
        if (dataDto != null && dataDto.user != null) {
            val token: String = dataDto.tokenType + BaseConstants.SPACE + dataDto.accessToken
            SharedPref.setToken(token)

            /*if onboared:true registration completed go to home */
            if (dataDto.user?.onboarded != null && dataDto.user?.onboarded == true) {
                SharedPref.setUser(dataDto.user)
                SharedPref.setUserType(dataDto.user?.type)
                if(dataDto.user?.type.equals("customer"))
                {
                    getHome(context)

                }
                else
                {
                    getSellerHome(context)

                }
            } else {
                val register = RegisterPageIntent(
                    type = dataDto.user?.type,
                    email = dataDto.user?.email,
                    phone = dataDto.user?.phone
                )
                getPermissionActivity(
                    context,
                    register
                )
            }
        } else {
            AlertUtil.ok(
                context = context,
                msg = context.getString(R.string.data_is_empty)
            )

        }
    }

    fun checkValue(value: String?): Boolean {
        if (value != null && value.isNotEmpty()) {
            return true
        }
        return false
    }

    fun getString(id: Int): String {
        return MyApplication.resourses.getString(id)
    }

    fun showBtn(progressbar: View, btn: View) {
        progressbar.hide()
        btn.show()
    }

    fun showProgess(progressbar: View, btn: View) {
        progressbar.show()
        btn.hide()
    }


    fun HideShowPassword(editText: TextInputEditText, imgView: ImageView) {
        if (editText.transformationMethod
                .equals(PasswordTransformationMethod.getInstance())
        ) {
            imgView.passwordOn()
            //Show Password
            editText.passwordShow()
        } else {
            imgView.passwordOf()
            //Hide Password
            editText.passwordHide()
        }
    }

    fun hasInternetConnection(application: MyApplication): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return false
    }


    fun addReplaceFragment(fragment: Fragment, activity: FragmentActivity?, addOrReplace: Int) {
        val FRAGMENT_TAG = "single"
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            when (addOrReplace) {
                IDConst.ADD_FRAGMENT -> {
                    add(R.id.flFragment, fragment, FRAGMENT_TAG).addToBackStack("TAG")
                    /*   detach(fragment)
                               attach(fragment)*/
                    commit()
                }
                IDConst.REPLACE_FRAGMENT -> {
                    replace(R.id.flFragment, fragment).addToBackStack(null)
                    commit()
                }
                IDConst.REPLACE_NO_BACKSTACK_FRAGMENT -> {
                    replace(R.id.flFragment, fragment)
                    commit()
                }
                else -> {}
            }
        }

    }

    fun appBarTitle(context: FragmentActivity?, name: Int) {
        if (context != null) {
            (context as UMainActivity?)?.appBarTitleName(context.resources.getString(name))
        }
    }


    fun hidekeyboard(context: Context, view: View?) {
        try {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getId(view: EditText): String {

        if (view.tag != null) {
            return view.tag as String
        }

        return BaseConstants.EMPTY
    }

    fun getId(view: View): String {
        if (view.tag != null) {
            return view.tag as String
        }

        return BaseConstants.EMPTY
    }

    fun getValue(view: EditText?): String {
        if (view != null) {
            return view.text.toString()
        }
        return BaseConstants.EMPTY
    }


    fun isGpsOn(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val statusOfGPS = manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return statusOfGPS
    }


    fun dismissAlert(
        dialog: AlertDialog?
    ) {
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun openWhatsApp(number: String?, Message: String, context: Context) {
        try {
            if (StringUtil.getString(number).isNotEmpty()) {
                val packageManager: PackageManager = context.getPackageManager()
                val i = Intent(Intent.ACTION_VIEW)
                val url =
                    "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(
                        Message,
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    context.startActivity(i)
                } else {
                    context.toast(context.getString(R.string.no_whatsapp_found))

                }
            } else {
                context.toast(context.getString(R.string.invalid_whatsapp_number))
            }
        } catch (e: java.lang.Exception) {
            Log.e("ERROR WHATSAPP", e.toString())
            context.toast(context.getString(R.string.no_whatsapp_found))
        }
    }

    fun calling(phoneNumber: String?, context: FragmentActivity) {
        if (StringUtil.getString(phoneNumber).isNotEmpty()) {

            context.lifecycleScope.launch {
                TedPermission.create()
                    .setRationaleTitle(context.getString(R.string.app_name))
                    .setDeniedTitle(context.getString(R.string.permission_denied))
                    .setDeniedMessage(
                        context.getString(R.string.reject_permission_desc)
                    )
                    .setGotoSettingButtonText(context.getString(R.string.setting))
                    .setPermissions(
                        Manifest.permission.CALL_PHONE,
                    )
                    .check().let {
                        if (it.isGranted) {
                            val intent =
                                Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber))
                            context.startActivity(intent)
                        } else {
                            Log.e("ted", "permissionResult: ${it.deniedPermissions}")
                        }
                    }
            }

        } else {
            context?.toast(getString(R.string.no_phone_number_Found))
        }

    }

}