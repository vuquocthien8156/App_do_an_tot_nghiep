package com.example.qthien.t__t.mvp.interactor

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import com.example.qthien.t__t.model.ResponseCatalogy
import com.example.qthien.t__t.model.ResponseInfoAddress
import com.example.qthien.t__t.model.ResponseProducts
import com.example.qthien.t__t.mvp.presenter.pre_frag_order.IPreFragOrder
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class InFragOrder(var context : Context, var iPreFragOrder: IPreFragOrder){

    val instance = RetrofitInstance.getRetrofit
    val mFusedLocationClient: FusedLocationProviderClient
    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
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
                    Log.d("latlnggggggggg" , "onLocationResult 1")
                    val locationAddress = getAddress(location.latitude, location.longitude)
                    val address = locationAddress?.getAddressLine(0)
                    iPreFragOrder.successGetLocationAddress("$address", LatLng( location.latitude, location.longitude))
                }
                else
                    iPreFragOrder.failure("Not found location")
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


    fun removeRequest() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getAddress(latitude: Double, longitude: Double): Address? {
        Log.d("TAGGG" , "getAddress -- $latitude vs $longitude")
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(context, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location fragment_result to returned, by documents it recommended 1 to 5
            return addresses[0]

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getProductsBestBuy(){
        val call = instance.getProductBestBuy()
        call.enqueue(object : Callback<ResponseProducts>{
            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {
                iPreFragOrder.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseProducts>, response: Response<ResponseProducts>) {
                val re = response.body()
                if(re?.status != null && re.status.equals("ok")){
                    iPreFragOrder.resultGetProductsBestBuy(re.arrProduct)
                }
            }
        })
    }

    fun getProductsByCatalogy(idCatelogy: Int?){
        val call = instance.getProductByCatalogy(idCatelogy)
        call.enqueue(object : Callback<ResponseProducts>{
            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {
                iPreFragOrder.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseProducts>, response: Response<ResponseProducts>) {
                val re = response.body()
                Log.d("TAGGGRessponse" , response.body().toString())
                if(re?.status != null && re.status.equals("ok")){
                    iPreFragOrder.resultGetProductsByCatalogy(re.arrProduct)
                }
            }

        })
    }

    fun getProductBestFavorite(){
        val call = instance.getProductBestFavorite()
        call.enqueue(object : Callback<ResponseProducts>{
            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {
                iPreFragOrder.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseProducts>, response: Response<ResponseProducts>) {
                val re = response.body()
                if(re?.status != null && re.status.equals("ok")){
                    iPreFragOrder.resultGetProductBestFavorite(re.arrProduct)
                }
             }

        })
    }

    fun getAllCatalogy(){
        val call = instance.getAllCatalogy()
        call.enqueue(object : Callback<ResponseCatalogy>{
            override fun onFailure(call: Call<ResponseCatalogy>, t: Throwable) {
                iPreFragOrder.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseCatalogy>, response: Response<ResponseCatalogy>) {
                val re = response.body()
                if(re?.status != null && re.status.equals("ok")){
                    iPreFragOrder.resultGetAllCatalogy(re.arrCatalogy)
                }
            }

        })
    }

    fun getAddressInfo(idCus: Int) {
        val call = instance.getAllAddressByUser(idCus , 1)
        call.enqueue(object : Callback<ResponseInfoAddress>{
            override fun onFailure(call: Call<ResponseInfoAddress>, t: Throwable) {
                iPreFragOrder.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseInfoAddress>, response: Response<ResponseInfoAddress>) {
                val re = response.body()
                Log.d("getAddressInfo" , re.toString())
                Log.d("getAddressInfo" , response.toString())
                if(re?.status != null && re.status.equals("Success")){
                    if(re.arrAddressInfo.size == 0)
                        iPreFragOrder.resultGetAddressDefault(null)
                    else
                        iPreFragOrder.resultGetAddressDefault(re.arrAddressInfo.get(0))
                }
            }

        })
    }
}