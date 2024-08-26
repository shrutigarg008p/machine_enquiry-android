package com.machine.machine.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.machine.machine.R
import com.machine.machine.databinding.ActivityMapsBinding


class MapSearchLocationActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private lateinit var binding: ActivityMapsBinding
    private var mGoogleMap: GoogleMap? = null
    private var mapFrag: SupportMapFragment? = null
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private val markerOptions = MarkerOptions()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /// supportActionBar!!.title = "Map Location Activity"
        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag!!.getMapAsync(this)
        binding.commonBtn.setOnClickListener {
            val latitiude = mLastLocation?.latitude
            val longitude = mLastLocation?.longitude
            Log.e("lat", "datat$latitiude")
            Log.e("long", "datat$longitude")
        }
    }

    public override fun onPause() {
        super.onPause()

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient!!, this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL


        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                buildGoogleApiClient()
                mGoogleMap!!.isMyLocationEnabled = true
                clickOnMap()

            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            buildGoogleApiClient()
            mGoogleMap!!.isMyLocationEnabled = true
            clickOnMap()
        }
    }

    fun clickOnMap() {
        mGoogleMap?.setOnMapClickListener { point ->
            mGoogleMap?.clear()


            markerOptions.position(point)
            markerOptions.title(" Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .snippet("")

            mGoogleMap?.addMarker(
                markerOptions
            )
        }

        mGoogleMap?.setOnMarkerClickListener { marker ->

            val LL: String =
                marker.title.toString() + " Lat:" + marker.position.latitude + " Long:" + marker.position.longitude
            Log.e("markert", LL)
            false
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority =
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient!!,
                mLocationRequest!!,
                this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onLocationChanged(location: Location) {
        Log.e("location1", "locaton${location.latitude}")
        Log.e("location2", "locaton${location.longitude}")

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        if (markerOptions != null) {
            mCurrLocationMarker = mGoogleMap!!.addMarker(markerOptions)
        }

        //Place current location marker
        /*     val latLng = LatLng(location.latitude, location.longitude)
             val markerOptions = MarkerOptions()
             markerOptions.position(latLng)
             markerOptions.title("Current Position")
             markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
             mCurrLocationMarker = mGoogleMap!!.addMarker(markerOptions)

             //move map camera
             mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
             mGoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(11F));*/

    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { dialogInterface, i -> //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@MapSearchLocationActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mGoogleMap!!.isMyLocationEnabled = true
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}