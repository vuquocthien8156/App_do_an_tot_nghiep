package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseMutilImage (
        @SerializedName("arr")
        var imagesUrl : ArrayList<String>
) : ResponseDefault()