package com.example.qthien.t__t.mvp.view.delivery_address

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.qthien.t__t.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var latLngCurrent: LatLng? = null
    lateinit var latLngCurrentMaps: LatLng
    private val AUTOCOMPLETE_REQUEST_CODE = 100
    val REQUEST_FINE_LOCATION = 101
    val  REQUEST_CHECK_SETTINGS = 102
    var dialogLoading : DialogLoadingFragment? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        dialogLoading = DialogLoadingFragment()
        dialogLoading?.setCancelable(false)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ibtnClose.setOnClickListener({
            finish()
        })

        ibtnLocation.setOnClickListener({
            dialogLoading?.show(supportFragmentManager , "dialog")
            buildDialogGoogleLocation()
        })

        loadLocationCreate()

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
                PlaceAutocomplete.MODE_OVERLAY
            )
                .setFilter(typeFilter)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        })

        btnSelectedAddress.setOnClickListener({
            val i = Intent()
            i.putExtra("address", edtAddressSelected.text.toString())
            setResult(Activity.RESULT_OK, i)
            finish()
        })
    }

    fun buildDialogGoogleLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        val builderr = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builderr.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builderr.build())
        result.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {
            override fun onComplete(task : Task<LocationSettingsResponse>) {
                try {
                    val response = task.getResult(ApiException::class.java)
                    getLocation()
                } catch (exception : ApiException) {
                    when (exception.getStatusCode()) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                            try {
                                dialogLoading?.dismiss()
                                val resolvable = exception as ResolvableApiException
                                resolvable.startResolutionForResult(
                                    this@MapsActivity , REQUEST_CHECK_SETTINGS)
                            } catch (e : IntentSender.SendIntentException) {
                                Log.i("Taggg", "PendingIntent unable to execute request.");
                            }
                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->{
                            Log.i("Taggg", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        }
                    }
                }
            }
        })
    }

    fun getLocationFromAddress(context : Context, strAddress : String) : LatLng?{
        val coder = Geocoder(context)
        val address : List<Address>
        var p1 : LatLng? = null

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            val location = address.get(0)
            p1 = LatLng (location.getLatitude(), location.getLongitude())

        } catch (ex : IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    override fun onStart() {
        super.onStart()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(this ,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                finish()
    }

    fun displayDialogGotoSettingLocation(){
        val builder = AlertDialog.Builder(this)
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        val message = getString(R.string.noti_gps);

        builder.setMessage(message)
            .setPositiveButton(R.string.settings, ({ d: DialogInterface, id: Int ->
                startActivity(Intent(action))
                d.dismiss()
            }))
            .setNegativeButton(R.string.close , ({ d: DialogInterface, id: Int -> }))
        builder.create().show();
    }

    fun loadLocationCreate(){
        val strInfo = getSharedPreferences("address", Activity.MODE_PRIVATE)?.getString("addressInfo", null)?.split("-")
        if(strInfo == null) {
            buildDialogGoogleLocation()
        }else{
            Log.d("addressLocation" , strInfo.get(1))
            latLngCurrent = getLocationFromAddress(this , strInfo.get(1))
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.d("latlnggggggggg" , "onLocationResult")
            if (locationResult == null) {
                Log.d("latlnggggggggg" , "onLocationResult Nulll")
                return
            }
            for (location in locationResult.locations) {
                if (location != null) {
                    dialogLoading?.dismiss()
                    latLngCurrent = LatLng(location.latitude, location.longitude)
                    moveCamera(latLngCurrent!!)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(20 * 1000)

        mFusedLocationClient.requestLocationUpdates(locationRequest , locationCallback , Looper.myLooper())
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

            if (midLatLng != null) {
                io.reactivex.Observable.create(ObservableOnSubscribe<LatLng> {
                    it.onNext(midLatLng)
                    it.onComplete()
                })
                    .subscribeOn(Schedulers.io())
                    .map {
                        getAddress(it.latitude, it.longitude)?.getAddressLine(0)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if(it != null){
                            edtAddressSelected.setText(it)
                        }
                    })
            }
        }
        Log.d("addressLocation" , "2222")

        if(latLngCurrent != null)
            moveCamera(latLngCurrent!!)
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
            .zoom(16f)                   // Sets the zoom
            .bearing(90f)                // Sets the orientation of the camera to east
            .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
            .build()                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mFusedLocationClient.removeLocationUpdates(locationCallback)
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

        if(REQUEST_CHECK_SETTINGS == requestCode){
            if(resultCode == Activity.RESULT_OK) {
                dialogLoading?.show(supportFragmentManager , "dialog")
                getLocation()
            }
            if(resultCode == Activity.RESULT_CANCELED) {
                displayDialogGotoSettingLocation()
            }
        }
    }
}
