package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderAdd (
    @SerializedName("thong_tin_DH")
    val infoOrder : Order,
    @SerializedName("Detail")
    val arrDetail : ArrayList<DetailOrder>
) : Parcelable