package com.example.qthien.t__t.model

data class ResponseEvaluation(
    var quantityVote : VoteQuantity?,
    var listEvaluttion : ArrayList<Evaluation>,
    var listIdEvaluation: ArrayList<String>
) : ResponseDefault()