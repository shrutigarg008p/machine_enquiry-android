package com.machine.machine.ui.user.screen.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.FragmentUAccountBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.setting.SettingVM
import kotlinx.android.synthetic.main.fragment_u_account.*
import java.io.File

class UAccountFragment : BaseFragramentLoaderVM<FragmentUAccountBinding>() {
    private var profilePic: String? = BaseConstants.EMPTY
    private var name :String = ""
    private var phone :String = ""
    private var email :String = ""
    private var profile :String = ""
    private lateinit var settingVM: SettingVM

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUAccountBinding.inflate(inflater, container, false)

    override fun appHeader() {
        EventBus.showActionBarTitleHideBottomNav(getString(R.string.account))
        name = FragmentUtil.getNameString(requireArguments())
        phone = FragmentUtil.getphoneString(requireArguments())
        email = FragmentUtil.getemailString(requireArguments())
        profile = FragmentUtil.getprofileString(requireArguments())
        account_name.setText(name)
        shopeadd_shopname.setText(email)
        shopeadd_phoneedt.setText(phone)
        account_image.loadUrl(profile)
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        settingVM = ViewModelProvider(this, factory)[SettingVM::class.java]
    }

    override fun viewModelObserver() {
        settingVM.editProfileLiveData.observe(this){event ->
            event.getContentIfNotHandled().let {response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.save.progress, binding.save.commonBtn)
                        context?.toast(getString(R.string.save))
                        EventBus.onBack()
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.save.progress, binding.save.commonBtn)
                        response.message?.let { message ->
                            binding.save.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.save.progress, binding.save.commonBtn)
                    }
                }
            }

        }
    }

    override fun viewClick() {
        binding.let {
            it.save.commonBtn.text = resources.getString(R.string.save)
            it.accountEditProfile.setOnClickListener {
                ImagePicker.with(this).crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080).start()
            }
            it.shopeaddChangePassword.setOnClickListener { NextPage(UChangePasswordFragment()) }
            it.save.commonBtn.setOnClickListener {saveRequest() }
            it.cancel.commonBtnNav.setOnClickListener { onCancel() }
            it.profileClick.setOnClickListener {

                ImagePicker.with(this).crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080).start()
            }

        }
    }

    override fun setup() {
        showContent()
    }


    fun onCancel() {
        EventBus.onBack()
    }

    fun NextPage(fragment: Fragment) {
        Utils.addReplaceFragment(
            fragment,
            activity,
            IDConst.ADD_FRAGMENT
        )

    }


    override fun onRetrybtn() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            val filePath: String? = context?.let { ImageUtil.getRealPathFromUri(uri, it) }
            profilePic = filePath

            binding.accountImage.setImageURI(uri)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            context?.toast(
                ImagePicker.getError(data)
            )
        } else {
            context?.toast(
                getString(R.string.task_canceled)
            )
        }
    }

    fun saveRequest(){
        val name = binding.accountName.text.toString()
        if (name.isEmpty()) {
            context?.let { AlertUtil.ok(it, getString(R.string.please_enter_name)) }
            return
        }

        val body = RequestBodies.editProfile(name, File(profilePic.toString()) )
        settingVM.editProfile(body)
    }

}
