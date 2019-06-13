package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoteQuantity(
        @SerializedName("tong")
        var total: Int,
        @SerializedName("namdiem")
        var totalFive: Int,
        @SerializedName("bondiem")
        var totalFour: Int,
        @SerializedName("badiem")
        var totalThree: Int,
        @SerializedName("haidiem")
        var totalTwo: Int,
        @SerializedName("motdiem")
        var totalOne: Int
) : Parcelable