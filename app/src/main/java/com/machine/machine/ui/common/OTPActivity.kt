package com.machine.machine.ui.common


import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityOtpValiatorBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.network.RetrofitInstance
import com.machine.machine.util.AlertUtil
import com.machine.machine.util.Utils
import com.machine.machine.util.errorSnack
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.OTPSendVM
import com.machine.machine.viewmodel.ViewModelProviderFactory

class OTPActivity : BaseActivity<ActivityOtpValiatorBinding>() {
    private lateinit var OTPSend: OTPSendVM
    private lateinit var email: String
    private lateinit var type: String
    private val TIMER_FOR_MCX: Long = 90000
    private var mCountDownTimer: CountDownTimer? = null

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityOtpValiatorBinding.inflate(layoutInflater)

    override fun setup() {
        getData()
        init()
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(application)
        OTPSend = ViewModelProvider(this, factory)[OTPSendVM::class.java]
    }

    private fun getData() {
        val intent: Intent = intent
        email = intent.getStringExtra(IDConst.INTENT_EMAIL_OR_PHONE).toString()
        type = intent.getStringExtra(IDConst.INTENT_USER_TYPE).toString()
    }

    private fun init() {
        optResendObserver()
        verifyOTPObserver()
        startTimer()
        editListener()
    }

    private fun editListener() {
        binding.run {
            addText(otp1, otp2)
            addText(otp2, otp3)
            addText(otp3, otp4)

            otpResend.setOnClickListener {
                resendOtpBtn()
            }
            otp.commonBtn.setOnClickListener {
                verifyOTPBtn()
            }
            navigate.setOnClickListener { finish() }
        }
    }

    private fun optResendObserver() {
        OTPSend.accountResp.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.pb.progressbtn, binding.otpResend)
                        toast(getString(R.string.otp_resend))
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.pb.progressbtn, binding.otpResend)
                        response.message?.let { message ->
                            binding.otp.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.pb.progressbtn, binding.otpResend)
                    }
                }
            }
        }
    }

    private fun verifyOTPObserver() {
        OTPSend.otpVerifyResp.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.otp.progress, binding.otp.commonBtn)
                        Utils.savedData(response.data?.data, this)

                        RetrofitInstance.clean()
                        toast(getString(R.string.otp_verified_successfully))
                        finish()
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.otp.progress, binding.otp.commonBtn)
                        response.message?.let { message ->
                            binding.otp.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.otp.progress, binding.otp.commonBtn)
                    }
                }
            }
        }
    }


    private fun clearOtpField() {
        binding.otp1.requestFocus()
        binding.otp1.text?.clear()
        binding.otp2.text?.clear()
        binding.otp3.text?.clear()
        binding.otp4.text?.clear()

    }


    private fun verifyOTPBtn() {
        Utils.hidekeyboard(this, currentFocus)
        if (Utils.checkValue(binding.otp1.text.toString()) &&
            Utils.checkValue(binding.otp2.text.toString()) &&
            Utils.checkValue(binding.otp3.text.toString()) &&
            Utils.checkValue(binding.otp4.text.toString())
        ) {
            val otp: String =
                binding.otp1.text.toString() + binding.otp2.text.toString() + binding.otp3.text.toString() + binding.otp4.text.toString()
            val body = RequestBodies.OtpVerfiy(
                otp,
                email
            )
            OTPSend.verifyOtp(body)

        } else {
            AlertUtil.ok(
                context = this,
                msg = getString(R.string.valid_Otp)
            )
        }
    }

    private fun resendOtpBtn() {
        Utils.hidekeyboard(this, currentFocus)
        clearOtpField()
        val body = RequestBodies.RegisterOtp(
            type,
            email
        )
        OTPSend.OtpSend(body)
    }


    private fun addText(view: EditText, viewNext: EditText) {
        view.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (view.text.toString().trim().length == 1) //size as per your requirement
                {
                    viewNext.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun startTimer() {
        try {
            val mTimeLeftInMillis = longArrayOf(TIMER_FOR_MCX)
            mCountDownTimer = object : CountDownTimer(TIMER_FOR_MCX, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    mTimeLeftInMillis[0] = millisUntilFinished
                    val calculateTimer = millisUntilFinished / 1000
                    binding.otpTimer.text = String.format(
                        "%d",
                        calculateTimer / 60
                    ) + BaseConstants.COLON + String.format("%02d", calculateTimer % 60)

                }

                override fun onFinish() {
                    finish()
                }
            }
            (mCountDownTimer as CountDownTimer).start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onCancelTimer() {
        mCountDownTimer?.cancel()

    }

    override fun onDestroy() {
        super.onDestroy()
        onCancelTimer()
    }
}