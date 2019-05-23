package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    @SerializedName("user_id")
    var idCustomer: Int,
    @SerializedName("fb_id")
    var id_fb: String?,
    @SerializedName("ten")
    var nameCustomer: String?,
    @SerializedName("sdt")
    var phoneNumber : String?,
    @SerializedName("gioi_tinh")
    var gender : Int?,
    @SerializedName("diem_tich")
    var point : Int?,
    @SerializedName("ngay_sinh")
    var birthday : String?,
    @SerializedName("email")
    var email : String?,
    @SerializedName("dia_chi")
    var address : String?,
    @SerializedName("avatar")
    var avatar : String?,
    var password : String?
) : Parcelable