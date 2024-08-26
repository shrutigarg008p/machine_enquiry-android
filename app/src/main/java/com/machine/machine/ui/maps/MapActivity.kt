package com.machine.machine.ui.maps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.machine.machine.R

class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private val PLACE_PICKER_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_maps)
        autoComPlace()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    fun autoComPlace() {
        /*   var fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
           var intent =
               Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
           startActivityForResult(intent, PLACE_PICKER_REQUEST)*/
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: ${place.name}, ${place.id}")

                mMap!!.clear()
                mMap!!.addMarker(
                    MarkerOptions().position(place.latLng).title(place.name.toString())
                )
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: $status")
            }
        })

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(-34.00, 151.00)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        clickOnMap()
    }

    fun clickOnMap() {
        mMap?.setOnMapClickListener { point ->
            mMap?.clear()
            val markerOptions = MarkerOptions()

            markerOptions.position(point)
            markerOptions.title(" Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .snippet("")

            mMap?.addMarker(
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

}