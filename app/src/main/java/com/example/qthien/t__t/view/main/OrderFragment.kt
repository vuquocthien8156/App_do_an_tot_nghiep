package com.example.qthien.t__t.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.qthien.t__t.adapter.FilterAdapter
import com.example.qthien.t__t.adapter.ViewPagerTabAdapter
import com.example.qthien.t__t.model.AddressDelivery
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.presenter.pre_frag_order.PreFragOrder
import com.example.qthien.t__t.view.cart.CartActivity
import com.example.qthien.t__t.view.delivery_address.DeliveryAddressActivity
import com.example.qthien.t__t.view.search_product.SearchProductActivity
import kotlinx.android.synthetic.main.fragment_orders.*




class OrderFragment : Fragment() , IViewFragOrder {

    companion object {
        fun newInstance() : OrderFragment = OrderFragment()
    }

    val REQUEST_FINE_LOCATION = 100
    val REQUEST_CODE_ADDRESS = 101
    var address : String? = null
    var infoDelivery : AddressDelivery? = null
    var latLngNow : LatLng? = null
    var arrCatalogyProduct : ArrayList<CatalogyProduct>? = null
    lateinit var adapterViewPager : ViewPagerTabAdapter
    var fragmentSelected : Fragment? = null
    var filter = false

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

        setLinetab()

        arrCatalogyProduct = ArrayList()
        PreFragOrder(context!! , this).getAllCatalogy()

        ibtnSearch.setOnClickListener({
            startActivity(Intent(context , SearchProductActivity::class.java))
        })

        ibtnFilter.setOnClickListener({
            loadMenuFilter(it)
        })

        btnChangeAddress.setOnClickListener({
            startActivityForResult(Intent(context , DeliveryAddressActivity::class.java) , REQUEST_CODE_ADDRESS)
        })

        txtAddressOrder.setOnClickListener({
            showAddress(txtAddressOrder.text.toString())
        })

        relaToDepay.setOnClickListener({
            startActivity(Intent(context , CartActivity::class.java))
        })

        viewpagerOrder.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                fragmentSelected = adapterViewPager.getRegisteredFragment(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun showAddress(address : String) {
//        FragmentDialogShowAddress.newInstance(address)
//            .show(childFragmentManager  , "dialog")
    }

    fun loadMenuFilter(view : View){
        val window = PopupWindow(context)
        val vieww = layoutInflater.inflate(com.example.qthien.t__t.R.layout.layout_menu_popupwindow , null)

        val listview = vieww.findViewById<ListView>(com.example.qthien.t__t.R.id.lvFilter)
        listview.adapter = FilterAdapter(context!! ,
                    arrCatalogyProduct?.map{ it.nameCatelogy }?.toMutableList() ?: mutableListOf())

        listview.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val catalogySelected = arrCatalogyProduct?.get(position)

                if(catalogySelected?.mainCatelogy == 1){
                    eventSelectedItemListView(3 , catalogySelected.idCatalogy)
                }
                else {
                    eventSelectedItemListView(2 , catalogySelected?.idCatalogy)
                }
                window.dismiss()
            }
        })

        listview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        window.contentView = vieww
        window.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.width = ViewGroup.LayoutParams.WRAP_CONTENT
        window.setOutsideTouchable(true)
        window.setFocusable(true)

        window.showAtLocation(view , Gravity.CENTER , 0 , 0)
    }

    fun eventSelectedItemListView(tabPosition : Int , idCatalogy : Int?){
        tabLayoutOrder.getTabAt(tabPosition)?.select()
        viewpagerOrder.setCurrentItem(tabPosition)
        if(fragmentSelected != null) {
            (fragmentSelected as TabFragment).setScrollToPositionFolowIdCata(idCatalogy)
        }
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
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.product_best_order))
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.product_like))
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.drink))
        arrTitle.add(context!!.getString(com.example.qthien.t__t.R.string.eat))

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
        Toast.makeText(context , "AAAAAAA" , Toast.LENGTH_LONG).show()
        finishLoader()
    }

    override fun resultGetAllCatalogy(arrCatalogy: ArrayList<CatalogyProduct>?) {
        if(arrCatalogy != null){
            arrCatalogy.remove(arrCatalogy.find { it.mainCatelogy == 3 })
            arrCatalogyProduct?.addAll(arrCatalogy)
            adapterViewPager = ViewPagerTabAdapter(childFragmentManager , addTitle(), arrCatalogyProduct)
            viewpagerOrder.adapter = adapterViewPager
            viewpagerOrder.offscreenPageLimit = 4
            tabLayoutOrder.setupWithViewPager(viewpagerOrder)
        }
    }

    fun finishLoader(){
        lnLoader.visibility = View.GONE
    }

    override fun resultGetProductsBestBuy(arrProduct: ArrayList<Product>?) {}
    override fun resultGetProductsByCatalogy(arrProduct: ArrayList<Product>?) {}
    override fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?) {}
}