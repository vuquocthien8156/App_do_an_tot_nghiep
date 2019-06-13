package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class DataEvaluation (
        @SerializedName("Vote")
        var quantityVote : VoteQuantity?,
        @SerializedName("ListEv")
        var listEvaluttion : ArrayList<Evaluation>,
        @SerializedName("list_thank")
        var listIdEvaluation: ArrayList<String>,
        @SerializedName("ListImg")
        var listUrlProduct : ArrayList<String>
)