package com.example.qthien.t__t.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailOrder(
    var idCart : Int,
    var quantity : Int,
    var unitPrice : Long,
    var size : String,
    var product: Product,
    var arrTopping : ArrayList<Product>?,
    var total : Long,
    var note : String
) : Parcelable