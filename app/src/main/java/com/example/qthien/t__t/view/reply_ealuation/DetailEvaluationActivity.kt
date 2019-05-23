package com.example.qthien.t__t.view.reply_ealuation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.EvaluationChildAdapter
import kotlinx.android.synthetic.main.activity_detail_evaluation.*

class DetailEvaluationActivity : AppCompatActivity() {

    lateinit var adapter : EvaluationChildAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_evaluation)

        adapter = EvaluationChildAdapter(this , arrayListOf())
        recyclerChild.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerChild.adapter = adapter

    }
}
