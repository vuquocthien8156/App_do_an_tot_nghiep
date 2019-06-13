package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseEvaluation(
        @SerializedName("obj")
        var data : DataEvaluation
) : ResponseDefault()