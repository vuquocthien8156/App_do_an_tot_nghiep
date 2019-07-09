package com.example.qthien.t__t.mvp.view.delivery_address

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.PlaceAutocompleteAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_search_deliverry_address.*
import java.util.*


class SearchDeliverryAddressActivity : AppCompatActivity() ,
    PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks{

    var mGoogleApiClient: GoogleApiClient? = null

    var mAdapter: PlaceAutocompleteAdapter? = null

    private val BOUNDS_INDIA = LatLngBounds(
        LatLng(-0.toDouble(), 0.toDouble()), LatLng(0.toDouble(), 0.toDouble())
    )

    var location : LatLng? = null
    val REQUEST_CODE_MAPS = 1
    val REQUEST_FINE_LOCATION = 2
    val REQUEST_CHECK_SETTINGS = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_search_deliverry_address)
        setSupportActionBar(toolbarSearchDelivery)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this , 0 , this)
            .addApi(Places.GEO_DATA_API)
            .build()

        location = intent.getParcelableExtra("latlngLocation")

        initRecylerAndAdapter()

        eventEdtSeatch()

        ibtnClear.setOnClickListener({
            edtSearchPlace.setText("")
            if(mAdapter!=null){
                mAdapter?.clearList()
            }
        })

        btnSelectOnMap.setOnClickListener({
            val strInfo = getSharedPreferences("address", Activity.MODE_PRIVATE)?.getString("addressInfo", null)?.split("-")
            if(strInfo != null && !strInfo.get(1).equals(""))
                checkPermissionLocation()
            else
                buildDialogGoogleLocation()
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
        result.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse>{
            override fun onComplete(task : Task<LocationSettingsResponse>) {
                try {
                    val response = task.getResult(ApiException::class.java)
                    startIntentMaps()
                } catch (exception : ApiException) {
                    when (exception.getStatusCode()) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                            try {
                                val resolvable = exception as ResolvableApiException
                                resolvable.startResolutionForResult(
                                    this@SearchDeliverryAddressActivity , REQUEST_CHECK_SETTINGS)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun startIntentMaps(){
        val i = Intent(this , MapsActivity::class.java)
        startActivityForResult(i , REQUEST_CODE_MAPS)
    }

    fun displayDialogGotoSettingLocation(){
        val builder = AlertDialog.Builder(this)
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        val message = getString(R.string.noti_gps);

        builder.setMessage(message)
            .setPositiveButton(R.string.settings, ({ d: DialogInterface, id: Int ->
                startActivity(Intent(action))
                d.dismiss()
            }))
            .setNegativeButton(R.string.close , ({ d: DialogInterface, id: Int -> }))
        builder.create().show();
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(REQUEST_CODE_MAPS == requestCode && resultCode == Activity.RESULT_OK && data != null){
            val add = data.extras?.getString("address")
            val i = Intent()
            i.putExtra("full_address" , add)
            setResult(RESULT_OK, i)
            finish()
        }

        if(REQUEST_CHECK_SETTINGS == requestCode){
            if(resultCode == Activity.RESULT_OK)
                startIntentMaps()
            if(resultCode == Activity.RESULT_CANCELED) {
                 displayDialogGotoSettingLocation()
            }
        }
    }

    fun checkPermissionLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(this ,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_FINE_LOCATION)
            else
                startIntentMaps()
        else
            startIntentMaps()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(REQUEST_FINE_LOCATION == requestCode){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startIntentMaps()
            }else
                openDialogPermisstion()
        }
    }

    fun openDialogPermisstion(){
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.noti)
        alert.setMessage(R.string.noti_location_permisstion)
        alert.setPositiveButton(R.string.settings , { dialog, which ->
            openSetting()
        })
        alert.setNegativeButton(R.string.close , { dialog, which ->
            finish()
        })
        alert.show()
    }

    fun openSetting(){
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", getPackageName(), null)
        intent.setData(uri)
        startActivity(intent)
    }

    private fun initRecylerAndAdapter() {
        val locale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0)
        } else {
            locale = getResources().getConfiguration().locale
        }
        val filter : AutocompleteFilter?
        filter =  AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
            .setCountry(locale.country)
            .build()
        Log.d("Contry" , locale.country)
        mAdapter = PlaceAutocompleteAdapter(this,
            com.example.qthien.t__t.R.layout.item_recy_search_place, mGoogleApiClient!! ,
            BOUNDS_INDIA,
            filter)
        recyclerPlace.layoutManager = LinearLayoutManager(this ,LinearLayoutManager.VERTICAL , false)
        recyclerPlace.adapter = mAdapter
    }

    private fun eventEdtSeatch() {
        edtSearchPlace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    ibtnClear.setVisibility(View.VISIBLE)
                    if (mAdapter != null) {
                        recyclerPlace.setAdapter(mAdapter)
                    }
                } else {
                    ibtnClear.setVisibility(View.GONE)
                }

                if(mGoogleApiClient != null){
                    if (s.toString() != "" && mGoogleApiClient!!.isConnected()) {
                        mAdapter?.getFilter()?.filter(s.toString())
                    } else if (!mGoogleApiClient!!.isConnected()) {
                        Log.e("mGoogleApiClient", "NOT CONNECTED")
                    }
                    Log.e("mGoogleApiClient", "CONNECTED")
                }
                Log.e("mGoogleApiClient", "C")
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    public override fun onStart() {
        mGoogleApiClient?.connect()
        super.onStart()

    }

    public override fun onStop() {
        mGoogleApiClient?.disconnect()
        super.onStop()
    }

    override fun onPlaceClick(mResultList: ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete>?, position: Int) {
        if (mResultList != null) {
            try {
                val placeId = mResultList[position].placeId.toString()

                val placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient!! , placeId)
                placeResult.setResultCallback(object : ResultCallback<PlaceBuffer> {
                    override fun onResult(places: PlaceBuffer) {
                        if (places.count == 1) {
                            //Do the things here on Click.....
                            val data = Intent(this@SearchDeliverryAddressActivity , DeliveryAddressActivity::class.java)
                            data.putExtra("lat", places.get(0).latLng.latitude.toString())
                            data.putExtra("lng", places.get(0).latLng.longitude.toString())
                            data.putExtra("address" , mResultList[position].seccondText.toString())
                            data.putExtra("full_address" , mResultList[position].fullText.toString())
                            data.putExtra("name_address" ,  mResultList[position].primaryText.toString())
                            setResult(RESULT_OK, data)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } catch (e: Exception) {

            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }
}
