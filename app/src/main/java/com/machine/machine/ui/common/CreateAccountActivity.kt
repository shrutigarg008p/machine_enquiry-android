package com.machine.machine.ui.common


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.DataHolder
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityCreateNewAccountBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.AlertUtil
import com.machine.machine.util.Utils
import com.machine.machine.util.ValidationUtil
import com.machine.machine.util.errorSnack
import com.machine.machine.viewmodel.OTPSendVM
import com.machine.machine.viewmodel.ViewModelProviderFactory
import kotlin.math.log

class CreateAccountActivity : BaseActivity<ActivityCreateNewAccountBinding>() {

    private lateinit var OTPSend: OTPSendVM
    private lateinit var emailOrPhone: String
    private lateinit var type: String

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityCreateNewAccountBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(application)
        OTPSend = ViewModelProvider(this, factory)[OTPSendVM::class.java]
        getOtpObserver()
    }

    override fun setup() {
        try {

            setText()
            userTypeDropdown()
            binding.account.commonBtn.setOnClickListener {
                sendOTPBtn()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun setText() {
        binding.account.commonBtn.text =
            getResources().getString(R.string.btn_send_confirmation_code)

    }

    private fun userTypeDropdown() {
        binding.edtUserType.setAdapter(this, DataHolder.getUserType())
        binding.edtUserType.setOnItemClickListener { parent, view, position, id ->
            //  LogUtil.e("number:  ", DataHolder.getUserType()[position].id);
            binding.edtUserType.tag = DataHolder.getUserType()[position].id
        }
    }


    private fun getOtpObserver() {
        OTPSend.accountResp.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.account.progress, binding.account.commonBtn)
                        OtpScreen(emailOrPhone, type)
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.account.progress, binding.account.commonBtn)
                        response.message?.let { message ->
                            binding.account.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.account.progress, binding.account.commonBtn)

                    }
                }
            }
        }
    }

    private fun sendOTPBtn() {
        Utils.hidekeyboard(this, currentFocus)
        type = Utils.getId(binding.edtUserType)
        emailOrPhone = binding.edtName.text.toString()

        if (type.isEmpty()) {
            AlertUtil.ok(this, getString(R.string.enter_user_type))
            return
        }
        Log.d("check_",ValidationUtil.checkEmailorPhone(this, emailOrPhone).toString() )
        if (!ValidationUtil.checkEmailorPhone(this, emailOrPhone)) {
            return
        }


        val body = RequestBodies.RegisterOtp(
            type,
            emailOrPhone
        )
        OTPSend.OtpSend(body)
    }

    fun OtpScreen(emailOrPhone: String, type: String) {
        Log.e("email", emailOrPhone)
        Intent(this, OTPActivity::class.java).also {
            it.putExtra(IDConst.INTENT_EMAIL_OR_PHONE, emailOrPhone)
            it.putExtra(IDConst.INTENT_USER_TYPE, type)
            startActivity(it)

        }
    }
}