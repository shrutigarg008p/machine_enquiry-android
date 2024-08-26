package com.machine.machine.ui.user.screen.setting.unused

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.adapter.ShopAddPicAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.FragmentUAddShopBinding
import com.machine.machine.model.PicItem
import com.machine.machine.model.common.DataField
import com.machine.machine.model.common.Resource
import com.machine.machine.model.requestBodies.CreateNewShopBody
import com.machine.machine.ui.seller.screen.SellerHomeFragment
import com.machine.machine.util.AlertUtil
import com.machine.machine.util.ImageUtil
import com.machine.machine.util.Utils
import com.machine.machine.viewmodel.RegisterVM
import com.machine.machine.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File
import java.util.*


class UAddShopFragment : BaseFragmentLoader<FragmentUAddShopBinding>(), View.OnClickListener,
    OnMapReadyCallback {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUAddShopBinding.inflate(inflater, container, false)
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var registerVm: RegisterVM
    private var locationValue: Boolean = false
    private lateinit var mMap: GoogleMap
    private lateinit var mTimePicker: TimePickerDialog
    private lateinit var HoursCall: String
    private var _HoursFirst: String = "first"
    private var _HoursLast: String = "last"
    private var workingDayList: java.util.ArrayList<String> = java.util.ArrayList()
    private var allPressedManual: String = BaseConstants.EMPTY
    private lateinit var picAdapter: ShopAddPicAdapter
    private var uploadList: java.util.ArrayList<PicItem> = java.util.ArrayList()
    lateinit var selectedLanguage: BooleanArray
    var spinnerList: ArrayList<DataField> = ArrayList()
    var langList: ArrayList<Int> = ArrayList()
    var langArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")
     var longitude: Double = 0.0
    var latitude: Double = 0.0
    private var mCurrLocationMarker: Marker? = null
    private val markerOptions = MarkerOptions()

    override fun setup() {

        EventBus.actionBarTitle(getString(R.string.add_a_shop))

        showContent()
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val factory = ViewModelProviderFactory(requireActivity().application)
        registerVm = ViewModelProvider(this, factory)[RegisterVM::class.java]
        getCategoryObserver()
        getTimePicker()
        binding.btn.commonBtn.setOnClickListener {

            submitData();

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

        binding.uploadBtn.setOnClickListener {
            if (uploadList.size >= 5) {
                AlertUtil.ok(
                    context = requireContext(),
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


        binding.hoursFirst.setOnClickListener {
            HoursCall = _HoursFirst
            mTimePicker.show()
        }
        binding.hoursLast.setOnClickListener {
            HoursCall = _HoursLast
            mTimePicker.show()
        }

    }


    private fun getCategoryObserver() {
        registerVm.categoryListAPi()
        registerVm.categoryList.observe(this) { response ->
            when (response) {
                is com.machine.machine.model.common.Resource.Success -> {

                    val objItem = ArrayList<DataField>()
                    response.data?.data.let {
                        if (it != null) {
                            for (obj in it) {
                                objItem.add(DataField(obj.id.toString(), obj.title))
                            }
                        }
                    }
                    spinnerList = objItem

                    categoryTypeDropdown(objItem)

                    binding.textView.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        // String array for alert dialog multi choice items
                        val colorsArray =
                            arrayOf("Black", "Orange", "Green", "Yellow", "White", "Purple")
                        // Boolean array for initial selected items
                        val checkedColorsArray = booleanArrayOf(
                            true, // Black checked
                            false, // Orange
                            false, // Green
                            true, // Yellow checked
                            false, // White
                            false  //Purple
                        )
                        // Convert the color array to list
                        val colorsList = Arrays.asList(*colorsArray)
                        //setTitle
                        builder.setTitle("Select Category")
                        //set multichoice
                        builder.setMultiChoiceItems(
                            colorsArray,
                            checkedColorsArray
                        ) { dialog, which, isChecked ->
                            // Update the current focused item's checked status
                            checkedColorsArray[which] = isChecked
                            // Get the current focused item
                            val currentItem = colorsList[which]
                            // Notify the current action
                            Toast.makeText(
                                context,
                                currentItem + " " + isChecked,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        // Set the positive/yes button click listener
                        builder.setPositiveButton("OK") { dialog, which ->
                            // Do something when click positive button
                            /*binding.textView.text = "Your preferred colors..... \n"
                            for (i in checkedColorsArray.indices) {
                                val checked = checkedColorsArray[i]
                                if (checked) {
                                    binding.textView.text =
                                        binding.textView.text.toString() + colorsList[i] + "\n"
                                }
                            }*/
                        }
                        // Set the neutral/cancel button click listener
                        builder.setNeutralButton("Cancel") { dialog, which ->
                            // Do something when click the neutral button
                        }
                        val dialog = builder.create()
                        // Display the alert dialog on interface
                        dialog.show()
                    }


                }
                else -> {

                }
            }
        }
    }

    private fun categoryTypeDropdown(listItem: ArrayList<DataField>) {
        binding.edtCategory.setAdapter(requireContext(), listItem)
        binding.edtCategory.setOnItemClickListener { parent, view, position, id ->
            //  LogUtil.e("number:  ", DataHolder.getUserType()[position].id);
            binding.edtCategory.tag = listItem[position].id
        }

    }


    override fun onRetrybtn() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        EventBus.hideActionBar()
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v) {
            // binding.head.headerBack -> Utils.onBack(activity)
            //binding.head.headerNotification -> Utils.onBack(activity)


            else -> {

            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        //  clickOnMap()
       // val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context,"lat in if"+PackageManager.PERMISSION_GRANTED.toString()
                    +"----long "+ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).toString(),Toast.LENGTH_SHORT).show()


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }


        mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            if (location == null) {
                requestNewLocationData()
                Toast.makeText(context,"lat outside if"+latitude.toString()+"----long "+longitude.toString(),Toast.LENGTH_SHORT).show()

            } else {
               latitude= location.getLatitude();
               longitude= location.getLongitude();
                Toast.makeText(context,"lat outside if"+latitude.toString()+"----long "+longitude.toString(),Toast.LENGTH_SHORT).show()

            }
        }


       // Add a marker in Sydney and move the camera
       val latLng = LatLng(latitude, longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        markerOptions.draggable(true)
        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        mMap?.addMarker(markerOptions)
    //    mMap.addMarker(MarkerOptions().position(location).title("Location"))
    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation

            latitude= mLastLocation.getLatitude();
            longitude= mLastLocation.getLongitude();
         //   latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude().toString() + "")
                //  longitTextView.setText("Longitude: " + mLastLocation.getLongitude().toString() + "")
        }
    }

    fun clickOnMap() {

        mMap?.setOnMapClickListener { point ->
            mCurrLocationMarker = null
            locationValue = true
            mMap?.clear()



            markerOptions.draggable(true)
            markerOptions.position(point)
            markerOptions.title(" Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .snippet("")

            mCurrLocationMarker = mMap?.addMarker(
                markerOptions
            )
        }

        mMap?.setOnMarkerClickListener { marker ->

            val LL: String =
                marker.title.toString() + " Lat:" + marker.position.latitude + " Long:" + marker.position.longitude
            Log.e("markert", LL)
            false
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

    private fun verticalAdapter() {
        val layoutManager = LinearLayoutManager(
            requireActivity().applicationContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.uploadRv.setHasFixedSize(false)
        binding.uploadRv.layoutManager = layoutManager
        picAdapter = ShopAddPicAdapter(uploadList)
        binding.uploadRv.adapter = picAdapter
    }

    private fun submitData() {
        var categoryArray = ArrayList<Int>()
        categoryArray.add( Utils.getId(binding.edtCategory).toString().toInt())

        val createShop = CreateNewShopBody(
            shopOwner = Utils.getValue(binding.ownerName),
            shopName = Utils.getValue(binding.shopName),
            shopEmail = Utils.getValue(binding.shopEmail),
            shopContact = Utils.getValue(binding.shopPhoneNumber),
            registrationNo = "",
            country = "",
            state = "",
            address_1 = Utils.getValue(binding.addLocation),
            address_2 = "",
            productCategories = categoryArray,
            workingHoursFrom = Utils.getValue(binding.hoursFirst).toString(),
            workingHoursTo = Utils.getValue(binding.hoursLast).toString(),
            workingDays = workingDayList,
            photos = uploadList,
            latitude = latitude.toString(),
            longitude = longitude.toString()
        )

        registerVm.createNewShop(createShop)
        registerVm.createNewShopResponse.observe(this, Observer{ response ->
            when (response) {
                is Resource.Success -> {

                    Toast.makeText(context,"Shop Add Successfully",Toast.LENGTH_LONG).show();
                    Utils.addReplaceFragment(
                        SellerHomeFragment(),
                        (getContext() as FragmentActivity),
                        IDConst.REPLACE_FRAGMENT)
                }

                is Resource.Error -> {
                    response.let { message ->

                        showError(message.errorCode)
                        // binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    //  showProgess()
                }
            }
        })

    }


    private fun getTimePicker() {

        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            requireContext(),
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

            val filePath: String? =
                ImageUtil.getRealPathFromUri(uri, requireActivity().applicationContext)
            uploadList.add(PicItem(pic_path = File(filePath)))
            verticalAdapter()
            Log.d("uploadList","${uploadList.size}")
            picAdapter.notifyDataSetChanged()
            // Use Uri object instead of File to avoid storage permissions
            //imgProfile.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}