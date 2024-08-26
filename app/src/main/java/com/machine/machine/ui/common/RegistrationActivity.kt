package com.machine.machine.ui.common


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityRegisterBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.requestBodies.RegisterBody
import com.machine.machine.model.requestBodies.RegisterPageIntent
import com.machine.machine.offline.SharedPref
import com.machine.machine.util.*
import com.machine.machine.viewmodel.RegisterVM
import com.machine.machine.viewmodel.ViewModelProviderFactory

class RegistrationActivity : BaseActivity<ActivityRegisterBinding>() {

    private lateinit var registerVm: RegisterVM

    private lateinit var registerIntent: RegisterPageIntent
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityRegisterBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        getData()
        if (Utils.isTypeCustomer(registerIntent.type)) {
            val factory = ViewModelProviderFactory(application)
            registerVm = ViewModelProvider(this, factory)[RegisterVM::class.java]
            getRegisterObserver()
        } else {
            setSellerHeader()
        }

    }

    override fun setup() {
        clickListener()
    }

    private fun setSellerHeader() {
        binding.btn.commonBtn.text = getString(R.string.next)
        binding.titleHeaderTxt.text = getString(R.string.shop_Account)
    }

    private fun getData() {
        val intent: Intent = intent
        intent.getParcelableExtra<RegisterPageIntent>(IDConst.INTENT_USER_REGISTRATION_DATA).let {
            if (it != null) {
                registerIntent = it
            } else {
                registerIntent = RegisterPageIntent()

            }
        }
    }


    private fun clickListener() {
        binding.btn.commonBtn.text = getString(R.string.btn_register)
        if (registerIntent.email != null) {
            binding.edtEmail.setText(registerIntent.email)
            binding.edtEmail.disable()
        }
        if (registerIntent.phone != null) {
            binding.edtPersonalPhone.setText(registerIntent.phone)
            binding.edtPersonalPhone.disable()

        }
        binding.btn.commonBtn.setOnClickListener {
            submitData()
        }
    }


    fun PasswordImg(view: View) {

        Utils.HideShowPassword(binding.edtPass, binding.showPassBtn)

    }

    private fun getFullNumber(): String {
        return binding.personalccp.selectedCountryCodeWithPlus
    }

    fun RePasswordImg(view: View) {
        Utils.HideShowPassword(binding.edtRePass, binding.showRePassBtn)

    }

    private fun getRegisterObserver() {
        registerVm.registerLD.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.btn.progress, binding.btn.commonBtn)
                        Utils.savedRegistrationData(response.data?.data, this)
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.btn.progress, binding.btn.commonBtn)
                        response.message?.let { message ->
                            binding.btn.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.btn.progress, binding.btn.commonBtn)

                    }
                }
            }
        }
    }

    private fun submitData() {
        try {
            Log.e("token", SharedPref.getToken()!!)
            Utils.hidekeyboard(this, currentFocus)
            val register = RegisterBody(
                name = Utils.getValue(binding.edtName),
                email = Utils.getValue(binding.edtEmail),
                profile_pic = null,
                phone_code = getFullNumber(),
                phone = Utils.getValue(binding.edtPersonalPhone),
                password = Utils.getValue(binding.edtPass),
                password_confirmation = Utils.getValue(binding.edtRePass),
                latitude = registerIntent.latitiude.toString(),
                longitude = registerIntent.longitude.toString()
            )

            if (register.name?.isEmpty() == true) {
                AlertUtil.ok(
                    context = this,
                    msg = getString(R.string.please_enter_name)
                )
                return
            }
            if (register.email?.isEmpty() == true) {
                AlertUtil.ok(
                    context = this,
                    msg = getString(R.string.please_enter_email)
                )
                return
            }
            if (register.phone?.isEmpty() == true) {
                AlertUtil.ok(
                    context = this,
                    msg = getString(R.string.please_enter_Phone)
                )
                return
            }
            if (!ValidationUtil.checkPhoneLength(this, register.phone)) {
                return
            }
            if (!ValidationUtil.checkPassword(
                    this,
                    register.password,
                    register.password_confirmation
                )
            ) {
                return
            }
            if (Utils.isTypeCustomer(registerIntent.type)) {
                registerVm.registerAPi(register, IDConst.CUSTOMER)
            } else {
                val intent = Intent(this, SellerRegistrationActivity::class.java)
                intent.putExtra(IDConst.INTENT_USER_REGISTRATION_DATA, register)
                startActivity(intent)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}