package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressDelivery(
    @SerializedName("ma_thong_tin")
    var idInfo : Int,
    @SerializedName("ten_nguoi_nhan")
    var nameCustomer : String,
    @SerializedName("dia_chi")
    var addressDelivery : String,
    @SerializedName("so_dien_thoai")
    var phoneNumber : String,
    @SerializedName("chinh")
    var main : Int,
    var delete : Int
) : Parcelable