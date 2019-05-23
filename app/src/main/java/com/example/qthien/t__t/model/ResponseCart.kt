package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

class ResponseCart(
    @SerializedName("Cart")
    var arrCart : ArrayList<MainProductCart>
) : ResponseDefault()