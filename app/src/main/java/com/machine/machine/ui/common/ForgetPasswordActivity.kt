package com.machine.machine.ui.common


import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityForgetBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.Utils
import com.machine.machine.util.ValidationUtil
import com.machine.machine.util.errorSnack
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.ForgetVM
import com.machine.machine.viewmodel.ViewModelProviderFactory


class ForgetPasswordActivity : BaseActivity<ActivityForgetBinding>() {

    lateinit var forgetVm: ForgetVM

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityForgetBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(application)
        forgetVm = ViewModelProvider(this, factory)[ForgetVM::class.java]
        optForgetobserver()
    }

    override fun setup() {
        init()

    }


    private fun init() {
        binding.forget.commonBtn.setOnClickListener {
            sumitRequest()
        }


    }

    private fun optForgetobserver() {
        forgetVm.forgetLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.forget.progress, binding.forget.commonBtn)
                        toast(getString(R.string.otp_send))
                        val email_phone = binding.emailPhone.text.toString()
                        openResetPage(email_phone)
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.forget.progress, binding.forget.commonBtn)
                        response.message?.let { message ->
                            binding.forget.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.forget.progress, binding.forget.commonBtn)
                    }
                }
            }
        }
    }

    fun sumitRequest() {
        val email_phone = binding.emailPhone.text.toString()
        if (!ValidationUtil.checkEmailorPhone(this, email_phone)) {
            return
        }
        val body = RequestBodies.forgetPassword(

            BaseConstants.EMPTY,
            email_phone,
            BaseConstants.EMPTY,
            BaseConstants.EMPTY
        )

        forgetVm.forgetApi(body)


    }

    fun openResetPage(email_phone: String) {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra(IDConst.INTENT_EMAIL_OR_PHONE, email_phone)
        startActivity(intent)
    }


}