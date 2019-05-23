package com.example.qthien.t__t.view.branch

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.BranchRootAdapter
import kotlinx.android.synthetic.main.activity_branch.*

class BranchActivity : AppCompatActivity() {

    lateinit var adapter : BranchRootAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch)

        setSupportActionBar(toolbarBranch)
        toolbarBranch.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setTitle(R.string.branch)

        adapter = BranchRootAdapter(this , arrayListOf())
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recy_branch.layoutManager = layoutManager
        recy_branch.adapter = adapter
    }
}
