package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseProducts(
    @SerializedName("list")
    var arrProduct : ArrayList<Product>?
) : ResponseDefault()