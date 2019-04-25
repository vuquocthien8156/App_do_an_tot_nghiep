package com.example.qthien.t__t.view.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.activity_search_product.*

class SearchProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        setSupportActionBar(toolbarSearch)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }
}
