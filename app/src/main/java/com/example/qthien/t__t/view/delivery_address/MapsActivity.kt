package com.example.qthien.t__t.view.delivery_address

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.qthien.t__t.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var latLngCurrent: LatLng
    lateinit var latLngCurrentMaps: LatLng
    private val AUTOCOMPLETE_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        latLngCurrent = intent.getParcelableExtra("latlngLocation")

        ibtnClose.setOnClickListener({
            finish()
        })

        ibtnLocation.setOnClickListener({
            getLocation()
        })

        edtAddressSelected.setOnClickListener({
            val locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = getResources().getConfiguration().getLocales().get(0)
            } else {
                locale = getResources().getConfiguration().locale
            }

            val typeFilter = AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setCountry(locale.country)
                    .build()

            val intent = PlaceAutocomplete.IntentBuilder(
                    PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        })

        getLocation()

        btnSelectedAddress.setOnClickListener({
            val i = Intent()
            i.putExtra("address", edtAddressSelected.text.toString())
            setResult(Activity.RESULT_OK, i)
            finish()
        })
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        val mFusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                latLngCurrent = LatLng(it.latitude, it.longitude)
                moveCamera(latLngCurrent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.isMyLocationEnabled = true

//        mMap.addMarker(MarkerOptions().position(latLngCurrent).draggable(true)
//                .title(getString(com.example.qthien.t__t.R.string.my_location)))
        mMap.setOnCameraIdleListener {
            //get latlng at the center by calling
            val midLatLng = mMap.getCameraPosition().target

            if(midLatLng != null) {
                io.reactivex.Observable.create(ObservableOnSubscribe<LatLng> {
                    it.onNext(midLatLng)
                    it.onComplete()
                })
                .subscribeOn(Schedulers.io())
                .map {
                    Log.i("latlnggggggggg", Thread.currentThread().getName());
                    getAddress(it.latitude, it.longitude)?.getAddressLine(0)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("latlnggggggggg", Thread.currentThread().getName());
                    edtAddressSelected.setText(it)
                })
            }
        }
    }

    fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
            ) // Here 1 represent max location fragment_result to returned, by documents it recommended 1 to 5
            return addresses.get(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun moveCamera(latlng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))

        val cameraPosition = CameraPosition.Builder()
                .target(
                        latlng
                )      // Sets the center of the map to location user
                .zoom(17f)                   // Sets the zoom
                .bearing(90f)                // Sets the orientation of the camera to east
                .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
                .build()                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                latLngCurrentMaps = place.latLng
                moveCamera(latLngCurrentMaps)
            }
        }
    }

}
