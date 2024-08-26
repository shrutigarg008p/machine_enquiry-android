package com.machine.machine.ui.common


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.ActivityLoginBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.DataDto
import com.machine.machine.network.RequestBodies
import com.machine.machine.offline.SharedPref
import com.machine.machine.util.AlertUtil
import com.machine.machine.util.Utils
import com.machine.machine.util.ValidationUtil
import com.machine.machine.util.errorSnack
import com.machine.machine.viewmodel.LoginVM
import com.machine.machine.viewmodel.ViewModelProviderFactory

class LoginActivity : BaseActivity<ActivityLoginBinding>(), View.OnClickListener {

    lateinit var loginVM: LoginVM
    var token =""

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(application)
        loginVM = ViewModelProvider(this, factory)[LoginVM::class.java]
        getLoginListener()
    }

    override fun setup() {

//        if (BuildConfig.DEBUG) {
//            binding.edtMail.setText("john.doe@email.com")
//            binding.edtPass.setText("password@123")
//        }
        setText()
        getDeviceToken()

    }

    private fun getDeviceToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            token = it.result //this is the token retrieved
            Log.d("Token::",token)
        }

    }

    private fun setText() {
        binding.let {
            it.login.commonBtn.text = getString(R.string.btn_login)
            it.reg.commonBtnNav.text = getString(R.string.register_now)
            it.login.commonBtn.setOnClickListener(this)
            it.reg.commonBtnNav.setOnClickListener(this)
            it.forgetPassword.setOnClickListener(this)
        }

    }


    override fun onClick(v: View?) {
        binding.run {
            when (v) {
                login.commonBtn -> otpClick()
                reg.commonBtnNav -> newAccount()
                forgetPassword -> forgetPassword()
                else -> {

                }
            }
        }
    }


    fun ShowHidePass(view: View) {
        Utils.HideShowPassword(binding.edtPass, binding.showPassBtn)
    }

    fun getLoginListener() {
        loginVM.loginResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.login.progress, binding.login.commonBtn)
                        savedData(response.data?.data)
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.login.progress, binding.login.commonBtn)
                        response.message?.let { message ->
                            binding.login.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.login.progress, binding.login.commonBtn)

                    }
                }
            }
        }
    }

    private fun otpClick() {

        Utils.hidekeyboard(this, currentFocus)

        val emailPhone = binding.edtMail.text.toString()
        val password = binding.edtPass.text.toString()

        if (!ValidationUtil.checkEmailorPhone(this, emailPhone)) {
            return
        }


        if (emailPhone.isNotEmpty() && password.isNotEmpty()) {
            val body = RequestBodies.LoginBody(
                emailPhone,
                password,
                token
            )

            loginVM.loginUser(body)

        } else {
            AlertUtil.ok(this, getString(R.string.enter_email_and_password))
        }
    }

    private fun savedData(dataDto: DataDto?) {
        if (dataDto != null) {
            val token: String = dataDto.tokenType + BaseConstants.SPACE + dataDto.accessToken
            SharedPref.setToken(token)
            SharedPref.setUser(dataDto.user)
            SharedPref.setUserType(dataDto.user?.type)
           if(dataDto.user?.type.equals("customer"))
           {
               Utils.getHome(this)

           }
            else
           {
               Utils.getSellerHome(this)

           }


        } else {
            binding.login.progress.errorSnack(
                getString(R.string.data_is_empty),
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun forgetPassword() {
        Intent(this@LoginActivity, ForgetPasswordActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun newAccount() {
        Intent(this@LoginActivity, CreateAccountActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun DUMMY() {
        Intent(this@LoginActivity, PermissionActivity::class.java).also {
            startActivity(it)
        }
    }


}