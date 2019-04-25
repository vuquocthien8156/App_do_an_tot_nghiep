package com.example.qthien.t__t.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import com.example.qthien.t__t.FragmentDialogShowAddress
import com.example.qthien.t__t.adapter.ViewPagerTabAdapter
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.presenter.pre_frag_order.PreFragOrder
import com.example.qthien.t__t.view.delivery_address.SearchDeliverryAddressActivity
import kotlinx.android.synthetic.main.fragment_orders.*
import java.net.URLEncoder


class OrderFragment : Fragment() , IViewFragOrder {

    companion object {
        fun newInstance() : OrderFragment = OrderFragment()
    }

    val REQUEST_FINE_LOCATION = 100
    var address : String? = null
    var latLngNow : LatLng? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(ActivityCompat.checkSelfPermission(context!! ,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity!! ,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION) ,
                    REQUEST_FINE_LOCATION)
        else {
            Log.d("TAGGG" , "Yes")
            PreFragOrder(context!! , this).getLocation()
        }

        return layoutInflater.inflate(com.example.qthien.t__t.R.layout.fragment_orders , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterViewPager = ViewPagerTabAdapter(childFragmentManager , addTitle())
        viewpagerOrder.adapter = adapterViewPager
        tabLayoutOrder.setupWithViewPager(viewpagerOrder)

        setLinetab()

        ibtnSearch.setOnClickListener({
            startActivity(Intent(context , SearchProductActivity::class.java))
        })

        ibtnFilter.setOnClickListener({
            loadMenuFilter(it)
        })

        btnChangeAddress.setOnClickListener({
            startActivity(Intent(context , SearchDeliverryAddressActivity::class.java))
        })

        txtAddressOrder.setOnClickListener({
            showAddress(txtAddressOrder.text.toString())
        })
    }

    private fun showAddress(address : String) {
        FragmentDialogShowAddress.newInstance(address)
            .show(childFragmentManager  , "dialog")
    }

    fun loadMenuFilter(view : View){
        val popupMenu = PopupMenu(context , view)
        popupMenu.menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_filter , popupMenu.menu)
        popupMenu.show()
    }

    fun setLinetab(){
        val root = tabLayoutOrder.getChildAt(0)
        if (root is LinearLayout) {
            root.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE)
            val drawable = GradientDrawable()
            drawable.setColor(Color.GRAY)
            drawable.setSize(2, 1)
            root.setDividerPadding(10)
            root.setDividerDrawable(drawable)
        }
    }

    fun addTitle() : ArrayList<String>{
        val arrTitle = ArrayList<String>()
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.product_buy_the_most))
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.product_like))
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.eat))
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.drink))

        return arrTitle
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.size < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context , com.example.qthien.t__t.R.string.permission_deny, Toast.LENGTH_SHORT).show()
            return
        }
        when(requestCode){
            REQUEST_FINE_LOCATION ->{
                PreFragOrder(context!! , this).getLocation()
            }
        }
    }

    override fun successGetLocationAddress(address: String, latLng: LatLng) {
        this.address = address
        this.latLngNow = latLng
        txtAddressOrder.setText(address)
        Toast.makeText(context , address , Toast.LENGTH_LONG).show()
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }
}