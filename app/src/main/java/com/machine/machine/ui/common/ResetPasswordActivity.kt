package com.machine.machine.ui.common


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityResetPasswordBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ForgetVM
import com.machine.machine.viewmodel.ViewModelProviderFactory

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    private lateinit var forgetVm: ForgetVM
    private lateinit var email_phone: String
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityResetPasswordBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        getData()
        val factory = ViewModelProviderFactory(application)
        forgetVm = ViewModelProvider(this, factory)[ForgetVM::class.java]
        optForgetobserver()
    }

    override fun setup() {

        init()

    }


    private fun init() {
        binding.reset.commonBtn.setOnClickListener {
            sumitRequest()
        }
    }

    private fun getData() {
        val intent: Intent = intent
        email_phone = intent.getStringExtra(IDConst.INTENT_EMAIL_OR_PHONE).toString()

    }

    fun PasswordImg(view: View) {
        Utils.HideShowPassword(binding.password, binding.showPassBtn)
    }

    fun RePasswordImg(view: View) {
        Utils.HideShowPassword(binding.passwordConfirm, binding.showRePassBtn)
    }

    private fun optForgetobserver() {
        forgetVm.forgetLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                        toast(getString(R.string.password_reset_successfully))
                        Utils.getLogin(applicationContext)

                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                        response.message?.let { message ->
                            binding.reset.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.reset.progress, binding.reset.commonBtn)
                    }
                }
            }
        }
    }

    fun sumitRequest() {
        val email_phone = email_phone
        val password = binding.password.text.toString()
        val confirm_password = binding.passwordConfirm.text.toString()
        val otp = binding.otp.text.toString()

        if (otp.isEmpty()) {
            AlertUtil.ok(this, getString(R.string.valid_Otp))
            return
        }
        if (!ValidationUtil.checkPassword(this, password, confirm_password)) {
            return
        }

        val body = RequestBodies.forgetPassword(
            otp,
            email_phone,
            password,
            confirm_password
        )

        forgetVm.forgetApi(body)
    }
}