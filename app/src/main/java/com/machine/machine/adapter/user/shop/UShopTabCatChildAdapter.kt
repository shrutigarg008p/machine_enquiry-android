package com.machine.machine.adapter.user.shop

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.Quatation
import com.machine.machine.QuatationDetails
import com.machine.machine.R
import com.machine.machine.adapter.seller.sellerRfq.SellerRfqListAdapter
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.UItemShopListBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.DistanceMatrixResponse
import com.machine.machine.model.response.user.ShopDto
import com.machine.machine.model.rfq.Data
import com.machine.machine.network.RetrofitInstance
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ShopDetailVM
import kotlinx.android.synthetic.main.u_item_shop_list.view.*
import retrofit2.Callback
import retrofit2.Response


class UShopTabCatChildAdapter(var context:Context,private var itemList: ArrayList<ShopDto>) :
    RecyclerView.Adapter<BaseAdapter>(), LocationListener {

    private lateinit var shopDetailVM: ShopDetailVM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {


        return BaseAdapter.create(parent, UItemShopListBinding::inflate)
    }

    private val MIN_DISTANCE_FOR_UPDATE: Float = 10F
    private val MIN_TIME_FOR_UPDATE = (1000 * 60 * 2).toLong()
    var lat:Double =0.0
    var long:Double =0.0
    protected var locationManager: LocationManager? = null
    var location: Location? = null
    internal lateinit var mLastLocation: Location


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        Log.e("positon", position.toString())
        nwlocation(holder);
        (holder.binding as UItemShopListBinding).let {
            //bind model to view
            val factory = ViewModelProviderFactory(Application())
            shopDetailVM = ViewModelProvider(context as ViewModelStoreOwner , factory)[ShopDetailVM::class.java]

            val item = itemList[position]
            Log.e("positon", ""+item.latitude)
            Log.e("positon", "psit"+item.longitude)
            Log.e("positon", "psit"+lat)
            Log.e("positon", "psit"+long)

//            shopDetailVM.distanceMatrixVM("$lat,$long","${item.latitude},${item.longitude}",BaseConstants.MODE,BaseConstants.LANGUAGEFORMAP,BaseConstants.KEY)


            val call : retrofit2.Call<DistanceMatrixResponse> =   RetrofitInstance.mapClientApi!!.distanceMatrixGoogleApiwihtout("$lat,$long","${item.latitude},${item.longitude}",BaseConstants.MODE,BaseConstants.LANGUAGEFORMAP,BaseConstants.KEY)
            call.enqueue(object : Callback<DistanceMatrixResponse> {
                override fun onResponse(call: retrofit2.Call<DistanceMatrixResponse>, response: Response<DistanceMatrixResponse>
                ) {

                    if(response.body()!!.rows[0].elements[0].status.toString()=="ZERO_RESULTS")
                    {
                        it.locationTxt.text="No Valid Location"

                    }
                    else
                    {

                            it.locationTxt.text=  response.body()!!.rows[0].elements[0].distance!!.text.toString()
                    }

}

                override fun onFailure(call: retrofit2.Call<DistanceMatrixResponse>, t: Throwable) {

                }

            })

            /*   var dist= distance_in_meter(lat,long,item.latitude,item.longitude)
               Log.e("dist",dist.toString())
               var newdist=Math.round(dist)
            */   if (item.images.isNotEmpty()){
                it.img.loadUrl(item.images[0])
            }else{
                it.img.loadUrl(item.shopLogo)
            }
            it.shopName.setData(item.shopName)
          /*  it.locationTxt.text =
                StringUtil.getString(newdist.toString()) + BaseConstants.SPACE + StringUtil.getString(
                    item.distanceUnit
                )*/
          //  getDistanceMatrix(holder)
            it.rating.setData(item.rating)
            it.locationAddress.setData(item.address)
            it.discountTxt.setData(item.offer)
            it.timing.setData(item.orderTiming)
            progressBtn(holder.binding, false)
            if (!item.isFavourite) {
                it.favbtn.setBackgroundColor(holder.itemView.context.getColor(R.color.dark_grey))
            } else {
                it.favbtn.setBackgroundColor(holder.itemView.context.getColor(R.color.appLight))
            }
            it.shopDirectionImg.setOnClickListener {

                val uri =
                    "http://maps.google.com/maps?saddr=" + lat + "," + long + "&daddr=" + item.latitude + "," + item.longitude
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps");
                holder.itemView.context.startActivity(intent)
            }

            it.favbtn.setOnClickListener {
                progressBtn(holder.binding, true)
                EventBus.addRemoveFavourite(EventItem(item.id, position))
            }

            holder.itemView.setOnClickListener {
                FragmentUtil.shopDetailPage(item.id, holder.itemView.context as FragmentActivity)


            }

        }

    }

    fun getDistanceMatrix(holder: BaseAdapter):String {
        var distance = ""
        shopDetailVM.distanceMatrixVM.observe(context as LifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                    response.data?.let { response ->
                        if (response.rows[0].elements[0].distance!=null){
                            distance = response.rows[0].elements[0].distance!!.text.toString()
                            holder.itemView.location_txt.text=distance
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                }
            }
        }

        return distance
    }


    fun progressBtn(it: UItemShopListBinding, show: Boolean) {
        if (show) {
            it.favbtn.hide()
            it.progress.show()
        } else {
            it.favbtn.show()
            it.progress.hide()
        }

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


    fun nwlocation(holder:BaseAdapter)
    {
        val nwLocation: Location? = getLocation(holder,LocationManager.NETWORK_PROVIDER)

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

    fun getLocation(holder:BaseAdapter,provider: String?): Location? {
        locationManager = holder.itemView.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(provider!!)) {
            if (ActivityCompat.checkSelfPermission(
                    holder.itemView.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    holder.itemView.context,
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
                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this
            )
            if (locationManager != null) {
                location = locationManager!!.getLastKnownLocation(provider)
                return location
            }
        }
        return null
    }


    fun addData(itemList: ArrayList<ShopDto>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }


    fun addFav(fav: Boolean?, position: Int, isNotError: Boolean) {
        if (isNotError) {
            if (itemList.isNotEmpty()) {
                itemList[position].isFavourite = fav!!

            }
        }
        notifyItemChanged(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = itemList.size
    override fun onLocationChanged(location: Location) {
        mLastLocation = location
    }

}