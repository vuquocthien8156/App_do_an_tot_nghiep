package com.example.qthien.t__t.interactor

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.model.ResponseCatalogy
import com.example.qthien.t__t.model.ResponseProducts
import com.example.qthien.t__t.presenter.pre_frag_order.IPreFragOrder
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class InFragOrder(var context : Context, var iPreFragOrder: IPreFragOrder){

    val instance = RetrofitInstance.getRetrofit

    @SuppressLint("MissingPermission")
    fun getLocation(){
        Log.d("TAGGG" , "DOO")

        val locationManager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isGPSEnable) {
            val mFusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            mFusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    Log.d("TAGGG" , "DOO2")
                    val locationAddress = getAddress(it.latitude, it.longitude)
                    val address = locationAddress?.getAddressLine(0)
                    iPreFragOrder.successGetLocationAddress("$address", LatLng( it.latitude, it.longitude))
                }
                else
                    iPreFragOrder.failure("Not found location")
            }
        }else{
            Toast.makeText(context , "Please enable GPS" , Toast.LENGTH_SHORT).show()
            return
        }
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


}