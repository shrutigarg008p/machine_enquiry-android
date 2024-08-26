package com.machine.machine.ui.user.screen.categoryShop

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.adapter.user.shop.UShopDetailProductAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.commonBase.SpacesItemDecoration
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.FragmentUShopDetailBinding
import com.machine.machine.databinding.IncludeShopDetailOverviewBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.DataObj
import com.machine.machine.model.response.user.ProductCategories
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ShopDetailVM
import kotlinx.coroutines.delay
import java.util.*


/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UShopDetailFragment : BaseFragramentLoaderVM<FragmentUShopDetailBinding>(),OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private lateinit var shopDetailVM: ShopDetailVM
    private lateinit var subCategoryAdapter: UShopDetailProductAdapter
    private var subCategoryList: ArrayList<ProductCategories> = ArrayList()
    private var shopId: Long = 0
    private var categoryId: Long? = 0
    private var catId: Long? = 0
    private var phoneNumber: String? = null
    private var sellerId: Long = 0
    private val MIN_DISTANCE_FOR_UPDATE: Float = 10F
    private val MIN_TIME_FOR_UPDATE = (1000 * 60 * 2).toLong()
    var lat:Double =0.0
    var long:Double =0.0

    private var mMap: GoogleMap? = null
    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationResult: LocationRequest
    internal lateinit var mLocationCallback: LocationCallback
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    protected var locationManager: LocationManager? = null
    var location: Location? = null
    private var whatsappNumber: String? = null

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUShopDetailBinding.inflate(inflater, container, false)

    override fun appHeader() {
        shopId = FragmentUtil.getIDLong(requireArguments())
        EventBus.actionBarTitle(getString(R.string.app_name))
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        shopDetailVM = ViewModelProvider(this, factory)[ShopDetailVM::class.java]
        shopDetailVM.getShopDetail(shopId)

    }

    override fun viewModelObserver() {
        getAddFavouriteVM()
        shopDetailVM.shopDetailVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data != null) {

                            if (response.data!!.categories.isNotEmpty()) {
                                subCategoryAdapter.addData(response.data!!.categories)
                            }
                            phoneNumber = response.data!!.communication?.call
                            whatsappNumber = response.data!!.communication?.whatsapp
                            sellerId = response.data!!.shop?.sellerId!!
                            setUI(response.data!!)


                        } else {
                            dataNotFound()

                        }
                    }
                }
                is Resource.Error -> {
                    showError(response.errorCode)

                }
                is Resource.Loading -> {
                    showProgess()
                }
            }
        }

    }

    fun getDistanceMatrix():String {
        var distance = ""
        shopDetailVM.distanceMatrixVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {

                    response.data?.let { response ->
                        if (response.rows[0].elements[0].distance!=null){
                            distance = response.rows[0].elements[0].distance!!.text.toString()
                            binding.shopDetail.locationTxt.text= distance
                        }
                    }
                }
                is Resource.Error -> {
                    showError(response.errorCode)

                }
                is Resource.Loading -> {
                }
            }
        }

        return distance
    }
    fun AppLocationService(context: Context) {

    }


    fun nwlocation()
    {
        val nwLocation: Location? = getLocation(LocationManager.NETWORK_PROVIDER)

        if (nwLocation != null) {
            val latitude = nwLocation.latitude
            val longitude = nwLocation.longitude

            lat =nwLocation.latitude
            long= nwLocation.longitude

         /*   Log.e("teston location",latitude.toString())
            Toast.makeText(
                requireContext(),
                """
            Mobile Location (NW): 
            Latitude: $latitude
            Longitude: $longitude
            """.trimIndent(),
                Toast.LENGTH_LONG
            ).show()*/
        } else {
            //showSettingsAlert("NETWORK")
        }
    }

    fun getLocation(provider: String?): Location? {
        locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(provider!!)) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null
            }
            locationManager!!.requestLocationUpdates(
                provider,
                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE,  this
            )
            if (locationManager != null) {
                location = locationManager!!.getLastKnownLocation(provider)
                return location
            }
        }
        return null
    }

    override fun viewClick() {

        binding.shopDetail.fav.btn.setOnClickListener {
            shopDetailVM.addFavouriteShop(shopId)
        }
        binding.shopWhatsapp.setOnClickListener {
            Log.e("whatsap", "whatsa")
            Utils.openWhatsApp(whatsappNumber, "Hi", activity as FragmentActivity)
        }

        binding.shopCall.setOnClickListener {

            Utils.calling(phoneNumber, activity as FragmentActivity)


        }

        binding.shopChat.setOnClickListener {
            createChatList()
        }
    }

    override fun onRetrybtn() {
        shopDetailVM.getShopDetail(shopId)
    }

    override fun setup() {
      /*  val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.isVisible=false*/
        gridSubCategory()
        nwlocation()
    }


    private fun gridSubCategory() {
        Log.d("catId", categoryId.toString())
        subCategoryAdapter = UShopDetailProductAdapter(subCategoryList, categoryId!!,catId!!)
        binding.include.rvList.addGridVerticalAdapter(activity, 2, subCategoryAdapter)
        binding.include.rvList.addItemDecoration(SpacesItemDecoration(2, 20, false))
    }

    private fun setUI(obj: DataObj) {
        if (obj.shop != null) {

            /*shop detail*/
            binding.shopDetail.let {
                val item = obj.shop!!
                catId=item.categories_shop[0].id
                subCategoryAdapter.addShopId(item.id)
                if (item.images!=null&& item.images.isNotEmpty()){
                    it.img.loadUrl(item.images[0])
                }else{
                    it.img.loadUrl(item.shopLogo)
                }
                it.shopName.setData(item.shopName)

             /*  var dist= distance_in_meter(lat,long,item.latitude,item.longitude)
                Log.e("dist",dist.toString())
                var newdist=Math.round(dist)

                if (lat == 0.0 && lat == 0.0){
                    it.locationTxt.text =   "0.0" + BaseConstants.SPACE + StringUtil.getString(
                        item.distanceUnit
                    )
                }else{
                    it.locationTxt.text =
                        StringUtil.getString(newdist.toString()) + BaseConstants.SPACE + StringUtil.getString(
                            item.distanceUnit
                        )
                }*/
                shopDetailVM.distanceMatrixVM("$lat,$long","${item.latitude},${item.longitude}",BaseConstants.MODE,BaseConstants.LANGUAGEFORMAP,BaseConstants.KEY)

                /*    it.locationTxt.text = getDistanceMatrix() + BaseConstants.SPACE + StringUtil.getString(
                    item.distanceUnit
                )*/
                it.rating.setData(item.rating)
                it.locationAddress.setData(item.address)
                it.discountTxt.setData(item.offer)
                it.timing.setData(item.orderTiming)
                it.shopBottomLine.hide()
                it.fav.btn.changeFavBtn(item.isFavourite ?: false, activity)
                getDistanceMatrix()

                it.shopDirectionImg.setOnClickListener {

                  /*  val uri: String =
                        java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", item.latitude, item.longitude)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    requireContext().startActivity(intent)

*/
                    val uri =
                        "http://maps.google.com/maps?saddr=" + lat + "," + long + "&daddr=" + item.latitude + "," + item.longitude
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent)

                }
            }
/*overview,service */
            binding.info.let { info ->
/*overview*/
                info.overview.setData(obj.overview)

                /*service*/

                service(info, obj)

                /*rating*/
                rating(info, obj)


            }
        }

    }

    private fun service(info: IncludeShopDetailOverviewBinding, obj: DataObj) {
        if (obj.services.isNotEmpty()) {
            info.shopServiceHeader.show()
            try {
                when (obj.services.size) {
                    1 -> {
                        info.serviceHeader1.setData(obj.services[0])
                    }
                    2 -> {
                        info.serviceHeader1.setData(obj.services[0])
                        info.serviceHeader2.setData(obj.services[1])
                    }
                    3 -> {
                        info.serviceHeader1.setData(obj.services[0])
                        info.serviceHeader2.setData(obj.services[1])
                        info.serviceHeader3.setData(obj.services[2])

                    }
                    4 -> {
                        info.serviceHeader1.setData(obj.services[0])
                        info.serviceHeader2.setData(obj.services[1])
                        info.serviceHeader3.setData(obj.services[2])
                        info.serviceHeader4.setData(obj.services[3])
                    }
                    else -> { // Note the block

                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            info.shopServiceHeader.hide()
        }
    }

    private fun rating(info: IncludeShopDetailOverviewBinding, obj: DataObj) {
        if (obj.ratings != null) {
            info.rating.let { rat ->
                rat.totalrating.setData(obj.ratings!!.overallAverage)
                rat.reviewtotal.setData(obj.ratings!!.totalStr)
                if (obj.ratings!!.categories != null)

                    rat.s.servicePb.progress = getRating(obj.ratings!!.categories?.service)
                rat.s.servicePbText.setData(obj.ratings!!.categories?.service)

                rat.d.deliveryPb.progress = getRating(obj.ratings!!.categories?.delivery)
                rat.d.deliveryPbText.setData(obj.ratings!!.categories?.delivery)

                rat.q.qualityPb.progress = getRating(obj.ratings!!.categories?.quality)
                rat.q.qualityPbText.setData(obj.ratings!!.categories?.quality)

                rat.p.pricePb.progress = getRating(obj.ratings!!.categories?.price)
                rat.p.pricePbText.setData(obj.ratings!!.categories?.price)

                if (obj.ratings!!.totalByStar.isNotEmpty()) {

                    try {
                        when (obj.ratings!!.totalByStar.size) {
                            1 -> {
                                rat.pb1.setData(obj.ratings!!.totalByStar[0].rating)
                                rat.pb1Review.setData(obj.ratings!!.totalByStar[0].totalStr)


                            }
                            2 -> {
                                rat.pb1.setData(obj.ratings!!.totalByStar[0].rating)
                                rat.pb2.setData(obj.ratings!!.totalByStar[1].rating)
                                rat.pb1Review.setData(obj.ratings!!.totalByStar[0].totalStr)
                                rat.pb2Review.setData(obj.ratings!!.totalByStar[1].totalStr)

                            }
                            3 -> {
                                rat.pb1.setData(obj.ratings!!.totalByStar[0].rating)
                                rat.pb2.setData(obj.ratings!!.totalByStar[1].rating)
                                rat.pb3.setData(obj.ratings!!.totalByStar[2].rating)
                                rat.pb1Review.setData(obj.ratings!!.totalByStar[0].totalStr)
                                rat.pb2Review.setData(obj.ratings!!.totalByStar[1].totalStr)
                                rat.pb3Review.setData(obj.ratings!!.totalByStar[2].totalStr)
                            }
                            4 -> {
                                rat.pb1.setData(obj.ratings!!.totalByStar[0].rating)
                                rat.pb2.setData(obj.ratings!!.totalByStar[1].rating)
                                rat.pb3.setData(obj.ratings!!.totalByStar[2].rating)
                                rat.pb4.setData(obj.ratings!!.totalByStar[3].rating)

                                rat.pb1Review.setData(obj.ratings!!.totalByStar[0].totalStr)
                                rat.pb2Review.setData(obj.ratings!!.totalByStar[1].totalStr)
                                rat.pb3Review.setData(obj.ratings!!.totalByStar[2].totalStr)
                                rat.pb4Review.setData(obj.ratings!!.totalByStar[3].totalStr)
                            }
                            5 -> {
                                rat.pb1.setData(obj.ratings!!.totalByStar[0].rating)
                                rat.pb2.setData(obj.ratings!!.totalByStar[1].rating)
                                rat.pb3.setData(obj.ratings!!.totalByStar[2].rating)
                                rat.pb4.setData(obj.ratings!!.totalByStar[3].rating)
                                rat.pb5.setData(obj.ratings!!.totalByStar[4].rating)

                                rat.pb1Review.setData(obj.ratings!!.totalByStar[0].totalStr)
                                rat.pb2Review.setData(obj.ratings!!.totalByStar[1].totalStr)
                                rat.pb3Review.setData(obj.ratings!!.totalByStar[2].totalStr)
                                rat.pb4Review.setData(obj.ratings!!.totalByStar[3].totalStr)
                                rat.pd5Review.setData(obj.ratings!!.totalByStar[4].totalStr)
                            }

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }
        }
    }


    private fun getRating(value: String?): Int {
        if (value != null) {
            return value.digitsOnly().toInt()
        }
        return 0

    }

    private fun createChatList() {
        var value: Int = sellerId.toInt()
        var sellerIdList: ArrayList<Int> = ArrayList()
        sellerIdList.add(value)

        shopDetailVM.addChatRequest(sellerIdList)
        shopDetailVM.createChatList.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            progressBtn(binding, false)
                            FragmentUtil.chattingPage(response.data?.channelId,"" ,"",context as FragmentActivity)

                        } else {
                            progressBtn(binding, false)
                        }
                    }
                }

                is Resource.Error -> {
                    progressBtn(binding, false)
                    response.message?.let { message ->
                        context?.toast(message)
                    }
                }

                is Resource.Loading -> {
                    progressBtn(binding, true)
                }
            }
        }
    }

    private fun getAddFavouriteVM() {

        shopDetailVM.addFavouriteShopVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            progressBtn(binding, false)
                            response.let { it ->
                                context?.toast(it.message)
                                it.data?.let { it1 ->

                                    binding.shopDetail.fav.btn.changeFavBtn(
                                        it1.isFavourite ?: false, activity
                                    )
                                }
                            }
                        } else {
                            progressBtn(binding, false)
                        }
                    }
                }

                is Resource.Error -> {
                    progressBtn(binding, false)
                    response.message?.let { message ->
                        context?.toast(message)
                    }

                }

                is Resource.Loading -> {
                    progressBtn(binding, true)
                }
            }
        }
    }

    private fun progressBtn(it: FragmentUShopDetailBinding, show: Boolean) {
        if (show) {
            it.shopDetail.fav.btn.hide()
            it.shopDetail.fav.progress.show()
        } else {
            it.shopDetail.fav.btn.show()
            it.shopDetail.fav.progress.hide()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

    }

    override fun onLocationChanged(location: Location) {

        mLastLocation = location
        Log.d("location>>",""+location.latitude +"  <<"+location.longitude)

    }

    override fun onConnected(p0: Bundle?) {
        Toast.makeText(context,"connected",Toast.LENGTH_SHORT).show()

        TODO("Not yet implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }
    private fun distance_in_meter(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371.0 // Radius of the earth in m
        val dLat = (lat1 - lat2) * Math.PI / 180f
        val dLon = (lon1 - lon2) * Math.PI / 180f
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180f) * Math.cos(
            lat2 * Math.PI / 180f
        ) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c =
            2f * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }


    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        Log.i("dist",dist.toString())
        dist = Math.acos(dist)
        Log.i("dist",dist.toString())
        dist = rad2deg(dist)
        Log.i("dist",dist.toString())
        dist = dist * 100 * 1.1515
        Log.i("dist",dist.toString())
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


}
