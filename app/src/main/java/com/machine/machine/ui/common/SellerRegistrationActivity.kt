package com.machine.machine.ui.common


import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.R
import com.machine.machine.adapter.ShopAddPicAdapter
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityShopRegisterBinding
import com.machine.machine.model.PicItem
import com.machine.machine.model.common.DataField
import com.machine.machine.model.common.Resource
import com.machine.machine.model.requestBodies.RegisterBody
import com.machine.machine.util.AlertUtil
import com.machine.machine.util.ImageUtil
import com.machine.machine.util.Utils
import com.machine.machine.util.errorSnack
import com.machine.machine.viewmodel.RegisterVM
import com.machine.machine.viewmodel.ViewModelProviderFactory
import java.io.File
import java.util.*


class SellerRegistrationActivity : BaseActivity<ActivityShopRegisterBinding>(),
    OnMapReadyCallback {

    private lateinit var registerVm: RegisterVM
    private lateinit var register: RegisterBody
    private lateinit var mTimePicker: TimePickerDialog
    private lateinit var HoursCall: String
    private var _HoursFirst: String = "first"
    private var _HoursLast: String = "last"
    private var workingDayList: ArrayList<String> = ArrayList()
    private var allPressedManual: String = BaseConstants.EMPTY
    private lateinit var picAdapter: ShopAddPicAdapter
    private var uploadList: ArrayList<PicItem> = ArrayList()
    private lateinit var mMap: GoogleMap
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityShopRegisterBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getData()
        val factory = ViewModelProviderFactory(application)
        registerVm = ViewModelProvider(this, factory)[RegisterVM::class.java]
        getCategoryObserver()
        getRegisterObserver()

    }

    private fun getCategoryObserver() {
        registerVm.categoryListAPi()
        registerVm.categoryList.observe(this) { response ->
            when (response) {
                is Resource.Success -> {

                    val objItem = ArrayList<DataField>()
                    response.data?.data.let {
                        if (it != null) {
                            for (obj in it) {
                                objItem.add(DataField(obj.id.toString(), obj.title))
                            }
                        }
                    }
                    categoryTypeDropdown(objItem)

                }
                else -> {

                }
            }
        }
    }

    private fun categoryTypeDropdown(listItem: ArrayList<DataField>) {
        binding.edtCategory.setAdapter(this, listItem)
        binding.edtCategory.setOnItemClickListener { parent, view, position, id ->
            //  LogUtil.e("number:  ", DataHolder.getUserType()[position].id);
            binding.edtCategory.tag = listItem[position].id
        }
    }

    override fun setup() {
        clickListener()
        verticalAdapter()
        getTimePicker()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val location = LatLng(register.latitude!!.toDouble(), register.longitude!!.toDouble())
        val markerOptions = MarkerOptions().position(location).title("I am here!")
        markerOptions.draggable(true)
     //   MarkerOptions().draggable(true)
        mMap.addMarker(markerOptions)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun getData() {
        val intent: Intent = intent
        intent.getParcelableExtra<RegisterBody>(IDConst.INTENT_USER_REGISTRATION_DATA).let {
            if (it != null) {
                register = it
            } else {
                register = RegisterBody()

            }
        }
    }

    private fun verticalAdapter() {
        val layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.uploadRv.setHasFixedSize(false)
        binding.uploadRv.layoutManager = layoutManager
        picAdapter = ShopAddPicAdapter(uploadList)
        binding.uploadRv.adapter = picAdapter
    }


    private fun clickListener() {
        binding.btn.commonBtn.text = getString(R.string.btn_register)
        binding.btn.commonBtn.setOnClickListener {
            submitData()
        }
        binding.hoursFirst.setOnClickListener {
            HoursCall = _HoursFirst
            mTimePicker.show()
        }
        binding.hoursLast.setOnClickListener {
            HoursCall = _HoursLast
            mTimePicker.show()
        }

        binding.uploadBtn.setOnClickListener {
            if (uploadList.size >= 5) {
                AlertUtil.ok(
                    context = this,
                    msg = getString(R.string.shop_photo_upload_limit)
                )
                return@setOnClickListener
            }
            ImagePicker.with(this)
                .crop(16.0f,9.0f)                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        binding.check.run {

            checkBoxListner(All, IDConst.ALL)
            checkBoxListner(Mon, IDConst.MONDAY)
            checkBoxListner(Tue, IDConst.TUESDAY)
            checkBoxListner(Wed, IDConst.WEDNESDAY)
            checkBoxListner(Thu, IDConst.THURSDAY)
            checkBoxListner(Fri, IDConst.FRIDAY)
            checkBoxListner(Sat, IDConst.SATURDAY)
            checkBoxListner(Sun, IDConst.SUNDAY)

        }


    }

    private fun getTimePicker() {

        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                if (HoursCall == _HoursFirst) {
                    binding.hoursFirst.setText(String.format("%02d:%02d", hourOfDay, minute))
                } else if (HoursCall == _HoursLast) {
                    binding.hoursLast.setText(String.format("%02d:%02d", hourOfDay, minute))
                }
                HoursCall = BaseConstants.EMPTY

                Log.e("time", String.format("%d : %d", hourOfDay, minute))
                // selectedTime.setText(String.format("%d : %d", hourOfDay, minute))
            }, hour, minute, false
        )
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
            var categoryArray = ArrayList<Int>()
            categoryArray.add( Utils.getId(binding.edtCategory).toString().toInt())

            Utils.hidekeyboard(this, currentFocus)
            register.shop_name = Utils.getValue(binding.shopName)
            register.shopOwnerName = Utils.getValue(binding.ownerName)
            register.shop_email = Utils.getValue(binding.shopEmail)
            register.shop_number = Utils.getValue(binding.shopPhoneNumber)
            register.category = categoryArray
            register.address_1 = Utils.getValue(binding.addLocation)
            register.working_hours_from = Utils.getValue(binding.hoursFirst)
            register.working_hours_to = Utils.getValue(binding.hoursLast)
            register.shop_imagePath = uploadList
            register.workingDayList = workingDayList
            registerVm.registerAPi(register, IDConst.SELLER)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun checkBoxListner(view: CheckBox, tag: String) {
        view.tag = tag
        view.setOnCheckedChangeListener { buttonView, isChecked ->
            Utils.getId(view).let {
                if (it.isNotEmpty()) {
                    if (isChecked) {
                        if (it == IDConst.ALL) {
                            allPressedManual = IDConst.AUTOMATIC
                            checkAll(true)
                        } else {
                            workingDayList.add(it)
                        }

                    } else {
                        if (allPressedManual != IDConst.MANUAL && it == IDConst.ALL) {
                            workingDayList.clear()
                            checkAll(false)
                        } else {

                            workingDayList.remove(it)
                            if (binding.check.All.isChecked) {
                                allPressedManual = IDConst.MANUAL
                                binding.check.All.isChecked = false
                            }
                        }
                    }
                }
            }
        }
    }


    private fun checkAll(value: Boolean) {
        binding.check.run {
            checked(Mon, value)
            checked(Tue, value)
            checked(Wed, value)
            checked(Thu, value)
            checked(Fri, value)
            checked(Sat, value)
            checked(Sun, value)
        }


    }

    private fun checked(view: CheckBox, value: Boolean) {

        view.isChecked = value
        if (value) {
            view.isPressed
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            val filePath: String? = ImageUtil.getRealPathFromUri(uri, applicationContext)
            uploadList.add(PicItem(pic_path = File(filePath)))
            picAdapter.notifyDataSetChanged()
            // Use Uri object instead of File to avoid storage permissions
            //imgProfile.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}