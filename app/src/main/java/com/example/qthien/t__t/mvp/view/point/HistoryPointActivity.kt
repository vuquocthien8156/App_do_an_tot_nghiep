package com.example.qthien.t__t.mvp.view.point

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.AdapterPointHistory
import com.example.qthien.t__t.model.Point
import com.example.qthien.t__t.mvp.presenter.point.PrePoint
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.activity_history_point.*

class HistoryPointActivity : AppCompatActivity() , IPointActi{

    override fun successgetAllPointByUser(arrPoint: ArrayList<Point>) {
        if(arrPoint.size > 0) {
            arrPointAll.addAll(arrPoint)
            arrPointFiter.addAll(arrPointAll)
            adapter.notifyDataSetChanged()

            val pointUse = arrPointAll.filter { it.type == 2}.sumBy { it.point }
            val pointHas = arrPointAll.filter { it.type == 1}.sumBy { it.point }
            val pointNow = pointHas - pointUse
            txtPointSurplus.setText("$pointNow")
            txtPointUse.setText("$pointUse")
            txtPointAccumulated.setText("$pointHas")
        }
        else
            relaContinuteOrder.visibility = View.VISIBLE
        progressLoader.visibility = View.GONE
        lnFooter.visibility = View.VISIBLE
    }

    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    lateinit var adapter : AdapterPointHistory
    lateinit var arrPointFiter : ArrayList<Point>
    lateinit var arrPointAll : ArrayList<Point>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_point)

        arrPointAll = ArrayList()

        arrPointFiter = ArrayList()
        arrPointFiter.addAll(arrPointAll)

        adapter = AdapterPointHistory(this , arrPointFiter)
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerPoint.layoutManager = layoutManager
        recyclerPoint.adapter = adapter

        txtNameCus.setText(customer?.nameCustomer)

        if(customer?.avatar != null)
            if(!customer?.avatar!!.contains("https://"))
                GlideApp.with(this).load(RetrofitInstance.baseUrl + "/" + customer?.avatar)
                    .into(imgAvataCus)
            else
                GlideApp.with(this).load(customer?.avatar).into(imgAvataCus)
        else
            GlideApp.with(this).load("${RetrofitInstance.baseUrl}/images/logo1.png")
                .into(imgAvataCus)

        btnPointAccumulated.setOnClickListener({
            if(btnPointAccumulated.text.equals(getString(R.string.show_point_accumulated)))
                btnPointAccumulated.setText(getString(R.string.hide_point_accumulated))
            else
                btnPointAccumulated.setText(getString(R.string.show_point_accumulated))
            filterPoint()
        })

        btnPointUse.setOnClickListener({
            if( btnPointUse.text.equals(getString(R.string.show_point_use)) )
                btnPointUse.setText(getString(R.string.hide_point_use))
            else
                btnPointUse.setText(getString(R.string.show_point_use))
            filterPoint()
        })

        ibtnCloseHisPoint.setOnClickListener({
            finish()
        })

        btnContinuteOrder.setOnClickListener({
            setResult(Activity.RESULT_OK , Intent())
            finish()
        })

        PrePoint(this).getAllPointByUser(customer!!.idCustomer)
    }

    fun filterPoint(){
        val bUse = btnPointUse.text.equals(getString(R.string.hide_point_use))
        val bAccumulated = btnPointAccumulated.text.equals(getString(R.string.hide_point_accumulated))
        arrPointFiter.clear()
        if(bUse && bAccumulated)
            arrPointFiter.addAll(arrPointAll)
        else
            if(bUse && !bAccumulated)
                arrPointFiter.addAll(arrPointAll.filter { it.type == 2 })
            if(bAccumulated && !bUse)
                arrPointFiter.addAll(arrPointAll.filter { it.type == 1 })
        adapter.notifyDataSetChanged()
    }
}
