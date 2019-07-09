package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseDiscount (
    @SerializedName("listSlide")
    var arrDiscount : ArrayList<Discount>
    ) : ResponseDefault()