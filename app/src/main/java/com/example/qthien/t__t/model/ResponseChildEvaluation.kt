package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseChildEvaluation (
        @SerializedName("list")
    var arrChildEvaluation : ArrayList<ChildEvaluation>
    ) : ResponseDefault()