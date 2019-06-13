package com.example.qthien.t__t.view.delivery_address

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
            checkPermissionLocation()
        })
    }

    fun startIntentMaps(){
        val i = Intent(this , MapsActivity::class.java)
        i.putExtra("latlngLocation" , location)
        startActivityForResult(i , REQUEST_CODE_MAPS)
    }

    fun checkGPS(){
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isGPSEnable) {
            startIntentMaps()
        }else{
            displayDialogGotoSettingLocation()
        }
    }

    fun displayDialogGotoSettingLocation(){
        val builder = AlertDialog.Builder(this)
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        val message = "Do you want open GPS setting?";

        builder.setMessage(message)
            .setPositiveButton("OK", ({ d: DialogInterface, id: Int ->
                startActivity(Intent(action))
                d.dismiss();
            }))
            .setNegativeButton("Cancel", ({ d: DialogInterface, id: Int ->

            }))
        builder.create().show();
    }

    fun checkPermissionLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this ,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FINE_LOCATION)
            else
                checkGPS()
        }
        else
            checkGPS()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(REQUEST_FINE_LOCATION == requestCode){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                checkGPS()
            else
                Toast.makeText(this , R.string.permission_deny , Toast.LENGTH_LONG).show()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(REQUEST_CODE_MAPS == requestCode && resultCode == Activity.RESULT_OK && data != null){
            val add = data.extras?.getString("address")
            val i = Intent()
            i.putExtra("full_address" , add)
            setResult(RESULT_OK, i)
            finish()
        }
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
                            Toast.makeText(applicationContext, mResultList[position].fullText.toString() , Toast.LENGTH_SHORT).show()

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }
}
