package com.example.qthien.t__t.view.delivery_address

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.adapter.PlaceAutocompleteAdapter
import com.example.qthien.t__t.model.Place
import com.example.qthien.t__t.view.MapsActivity
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


class SearchDeliverryAddressActivity : AppCompatActivity() , IViewSearchDelivery ,
    PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks{

    var mGoogleApiClient: GoogleApiClient? = null

    var mAdapter: PlaceAutocompleteAdapter? = null

    private val BOUNDS_INDIA = LatLngBounds(
        LatLng(-0.toDouble(), 0.toDouble()), LatLng(0.toDouble(), 0.toDouble())
    )

    var location : LatLng? = null
    val REQUEST_CODE_MAPS = 1

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

        initRecylerAndAdapter()

        eventEdtSeatch()

        ibtnClear.setOnClickListener({
            edtSearchPlace.setText("")
            if(mAdapter!=null){
                mAdapter?.clearList()
            }
        })

        btnSelectOnMap.setOnClickListener({
            startActivityForResult(Intent(this , MapsActivity::class.java) , REQUEST_CODE_MAPS)
        })
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
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

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
                            val data = Intent()
                            data.putExtra("lat", places.get(0).latLng.latitude.toString())
                            data.putExtra("lng", places.get(0).latLng.longitude.toString())
                            data.putExtra("address" , mResultList[position].seccondText)
                            data.putExtra("name_address" ,  mResultList[position].primaryText)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun successSearchPlace(arrPlace: ArrayList<Place>) {

    }

    override fun failureSearchPlace(message: String) {
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }
}
