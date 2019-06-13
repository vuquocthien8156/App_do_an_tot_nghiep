package com.example.qthien.t__t.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.qthien.t__t.FragmentDialogShowAddress
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.FilterAdapter
import com.example.qthien.t__t.adapter.ViewPagerTabAdapter
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.presenter.pre_frag_order.PreFragOrder
import com.example.qthien.t__t.view.cart.CartActivity
import com.example.qthien.t__t.view.delivery_address.DeliveryAddressActivity
import com.example.qthien.t__t.view.search_product.SearchProductActivity
import kotlinx.android.synthetic.main.fragment_orders.*
import java.text.DecimalFormat


class OrderFragment : Fragment() , IViewFragOrder{

    interface OrderFragmentCallMain{
        fun visibleNavigation(boolean: Boolean)
     }

    companion object {
        fun newInstance() : OrderFragment = OrderFragment()
    }

    private val REQUEST_FINE_LOCATION = 100
    private val REQUEST_CODE_ADDRESS = 101
    private var addressInfo : InfoAddress? = null
    private var latLngNow : com.google.android.gms.maps.model.LatLng? = null
    private var arrCatalogyProduct : ArrayList<CatalogyProduct>? = null
    private var adapterViewPager : ViewPagerTabAdapter? = null
    private var fragmentSelected : Fragment? = null
    private var orderFragmentCallMain : OrderFragmentCallMain? = null
    var checkGPSFirst = false
    lateinit var preFragOrder : PreFragOrder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preFragOrder = PreFragOrder(context!! , this@OrderFragment)
        return layoutInflater.inflate(com.example.qthien.t__t.R.layout.fragment_orders , container , false)
    }

    fun initLayoutLocation(){
        lnLocation.visibility = View.VISIBLE
        btnGPS.setOnClickListener({
            val locationManager : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if(isGPSEnable) {
                preFragOrder.getLocation()
                lnLocation.visibility = View.GONE
            }
            else {
                val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                startActivity(Intent(action))
            }
        })
        btnAddressDefaul.setOnClickListener({
            preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
            lnLocation.visibility = View.GONE
        })
        btnCancel.setOnClickListener({
            txtAddressOrder.setText(R.string.not_address_delivery)
        })

        checkGPSFirst = true
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(context , "onStart" , Toast.LENGTH_LONG).show()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FINE_LOCATION)
            else
                checkGPS()
        }
        else
            checkGPS()
    }


    fun checkGPS(){
        val locationManager : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isGPSEnable) {
            lnLocation.visibility = View.GONE
            checkGPSFirst = true
            if(addressInfo == null)
                preFragOrder.getLocation()
        }else{
            if(addressInfo == null && !checkGPSFirst)
                initLayoutLocation()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun resultGetAddressDefault(infoAddress: InfoAddress?) {
        if(infoAddress != null) {
            addressInfo = infoAddress.copy()
            txtAddressOrder.setText("${addressInfo?.nameCustomer} - ${addressInfo?.addressInfo} - ${addressInfo?.phoneCustomer}")
        }else {
            val address = context?.getSharedPreferences("address" , Context.MODE_PRIVATE)?.
                    getString("address" , "")
            if(address.equals(""))
                txtAddressOrder.setText(R.string.not_address_delivery)
            else
                txtAddressOrder.setText(address)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OrderFragmentCallMain)
            orderFragmentCallMain = context
    }

    @SuppressLint("SetTextI18n")
    fun setUpQuantityAndPrice(){
        val share = context!!.getSharedPreferences("QuantityPrice" , Activity.MODE_PRIVATE)
        val quantity = share.getInt("quantity" , 0)
        val price = share.getInt("price" , 0)

        if(quantity != 0){
            relaToDepay.visibility = View.VISIBLE
            txtQuantity.setText(quantity.toString())
            txtSumPriceCart.setText(DecimalFormat("###,###,###").format(price)+ " đ")
        }
        else
            relaToDepay.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLinetab()

        arrCatalogyProduct = ArrayList()
        preFragOrder.getAllCatalogy()

        ibtnSearch.setOnClickListener({
            startActivity(Intent(context , SearchProductActivity::class.java))
        })

        ibtnFilter.setOnClickListener({
            loadMenuFilter(it)
        })

        btnChangeAddress.setOnClickListener({
            val i = Intent(context , DeliveryAddressActivity::class.java)
            startActivityForResult(i , REQUEST_CODE_ADDRESS)
        })

        txtAddressOrder.setOnClickListener({
            showAddress()
        })

        relaToDepay.setOnClickListener({
            startActivity(Intent(context , CartActivity::class.java))
        })

        viewpagerOrder.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                fragmentSelected = adapterViewPager?.getRegisteredFragment(position)
                (fragmentSelected as TabFragment).checkBottomRecycler()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        setUpQuantityAndPrice()
    }

    fun updateViewPager(){
        if(adapterViewPager != null) {
            adapterViewPager?.notifyDataSetChanged()
        }
    }

    private fun showAddress() {
        if(addressInfo != null)
            FragmentDialogShowAddress.newInstance(addressInfo)
            .show(childFragmentManager  , "dialog")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_ADDRESS && resultCode == Activity.RESULT_OK && data != null){
            addressInfo = data.extras?.getParcelable("address")
            txtAddressOrder.setText("${addressInfo?.nameCustomer} - ${addressInfo?.addressInfo} - ${addressInfo?.phoneCustomer}")
        }
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
                preFragOrder.getLocation()
            }
        }
    }

    override fun successGetLocationAddress(address: String, latLng: com.google.android.gms.maps.model.LatLng) {
        this.addressInfo = InfoAddress(0 , "" , "" , address , 0 , 0 , 0)
        this.latLngNow = latLng
        txtAddressOrder.setText(address)
        context?.getSharedPreferences("address" , Context.MODE_PRIVATE)?.
                edit()?.putString("address" , address)?.apply()
        preFragOrder.removeRequest()
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
//        finishLoader()
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

    fun callVisibleNavigationMain(boolean: Boolean){
        orderFragmentCallMain?.visibleNavigation(boolean)
    }

    fun visibleBtnFilter(boolean: Boolean){
        val animHide = AnimationUtils.loadAnimation(context , R.anim.anim_hide_btn_filter)
        val animShow = AnimationUtils.loadAnimation(context , R.anim.anim_show_btn_filter)
        if(boolean){
            if(ibtnFilter.visibility  != View.GONE)
                ibtnFilter.startAnimation(animHide)
            ibtnFilter.visibility = View.GONE
        }
        else{
            if(ibtnFilter.visibility  != View.VISIBLE)
                ibtnFilter.startAnimation(animShow)
            ibtnFilter.visibility = View.VISIBLE
        }
    }

    fun finishLoader(){
        lnLoader.visibility = View.GONE
    }

    override fun resultGetProductsBestBuy(arrProduct: ArrayList<Product>?) {}
    override fun resultGetProductsByCatalogy(arrProduct: ArrayList<Product>?) {}
    override fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?) {}
}