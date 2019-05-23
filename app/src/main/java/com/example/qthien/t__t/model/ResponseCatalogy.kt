package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseCatalogy(
    @SerializedName("listCatalogy")
    var arrCatalogy : ArrayList<CatalogyProduct>
) : ResponseDefault()