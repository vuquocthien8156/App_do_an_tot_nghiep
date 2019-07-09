package com.example.qthien.t__t.mvp.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.Log
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
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.mvp.presenter.pre_frag_order.PreFragOrder
import com.example.qthien.t__t.mvp.view.cart.CartActivity
import com.example.qthien.t__t.mvp.view.delivery_address.DeliveryAddressActivity
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import com.example.qthien.t__t.mvp.view.order.OrderHistoryActivity
import com.example.qthien.t__t.mvp.view.search_product.SearchProductActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
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
    private val REQUEST_CART = 102
    private val REQUEST_CHECK_SETTINGS = 105
    private var latLngNow : com.google.android.gms.maps.model.LatLng? = null
    var hasAddress = false
    private var arrCatalogyProduct : ArrayList<CatalogyProduct>? = null
    private var adapterViewPager : ViewPagerTabAdapter? = null
    private var fragmentSelected : Fragment? = null
    private var orderFragmentCallMain : OrderFragmentCallMain? = null
    var checkGPSFirst = false
    private var discount : Discount? = null
    lateinit var preFragOrder : PreFragOrder
    var scrollDowm = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preFragOrder = PreFragOrder(context!! , this@OrderFragment)
        return layoutInflater.inflate(com.example.qthien.t__t.R.layout.fragment_orders , container , false)
    }

    fun initLayoutLocation(){
        swipeRefreshLocation.visibility = View.VISIBLE
        val locationManager : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btnGPS.setOnClickListener({
            val isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if(isGPSEnable) {
                buildDcialogGoogleLocation()
            }
            else {
                val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                startActivity(Intent(action))
            }
        })
        btnAddressDefaul.setOnClickListener({
            preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
            swipeRefreshLocation.visibility = View.GONE
        })
        btnCancel.setOnClickListener({
            preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
            swipeRefreshLocation.visibility = View.GONE
        })

        swipeRefreshLocation.setOnRefreshListener {
            buildDcialogGoogleLocation()
        }
    }

    fun buildDcialogGoogleLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        val builderr = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builderr.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(activity!!).checkLocationSettings(builderr.build())
        result.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {
            override fun onComplete(task : Task<LocationSettingsResponse>) {
                try {
                    val response = task.getResult(ApiException::class.java)
                    checkGPS(true)
                } catch (exception : ApiException) {
                    when (exception.getStatusCode()) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                            try {
                                if(!checkGPSFirst){
                                    val resolvable = exception as ResolvableApiException
                                    this@OrderFragment.startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null)
                                }
                                else
                                    if(latLngNow == null && !txtAddressOrder.text.toString().trim().equals(context?.getString(R.string.not_address_delivery)?.trim()) ){
                                        preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
                                        Toast.makeText(context , R.string.not_my_location , Toast.LENGTH_LONG).show()
                                    }
                                checkGPS(false)
                            } catch (e : IntentSender.SendIntentException) {
                                Log.i("Taggg", "PendingIntent unable to execute request.");
                            }
                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->{
                            preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
                            Toast.makeText(context , R.string.not_my_location , Toast.LENGTH_LONG).show()
                            swipeRefreshLocation.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    fun checkGPS(boolean: Boolean){
        Log.d("checkGPS" , boolean.toString())
        if(boolean) {
            swipeRefreshLocation.visibility = View.GONE
            checkGPSFirst = true
            if(!hasAddress) {
                Log.d("checkGPS" , "dô 1")
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_FINE_LOCATION
                        )
                    else
                        preFragOrder.getLocation()
                }
                else
                    preFragOrder.getLocation()
            }

        }else{
            swipeRefreshLocation.isRefreshing = false
            if(!hasAddress && !checkGPSFirst)
                initLayoutLocation()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden)
            buildDcialogGoogleLocation()
        if(scrollDowm) orderFragmentCallMain?.visibleNavigation(false)
    }

    @SuppressLint("SetTextI18n")
    override fun resultGetAddressDefault(infoAddress: InfoAddress?) {
        checkGPSFirst = true
        if(infoAddress != null) {
            hasAddress = true
            val addressInfoStr = "${infoAddress.nameCustomer} - ${infoAddress.addressInfo} - ${infoAddress.phoneCustomer}"
            txtAddressOrder.setText(addressInfoStr)
            context?.getSharedPreferences("address" , Context.MODE_PRIVATE)?.
                edit()?.putString("addressInfo" , addressInfoStr)?.apply()
        }else {
            val arrInfo =
                context?.getSharedPreferences("address", Activity.MODE_PRIVATE)?.getString("addressInfo", null)?.split("-")
            if(arrInfo == null) {
                txtAddressOrder.setText(R.string.not_address_delivery)
            }else {
                hasAddress = true
                val addressInfoStr = "${arrInfo.get(0)} - ${arrInfo.get(1)} - ${arrInfo.get(2)}"
                txtAddressOrder.setText(addressInfoStr)
            }
        }
        swipeRefreshLocation.visibility = View.GONE
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
            val i = Intent(context , SearchProductActivity::class.java)
            i.putExtra("discount" , discount)
            startActivity(i)
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
            val i = Intent(context , CartActivity::class.java)
            i.putExtra("discount" , discount)
            startActivityForResult(i , REQUEST_CART)
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

    fun getDiscount() : Discount? = discount

    fun updateViewPager(){
        if(adapterViewPager != null) {
            adapterViewPager?.notifyDataSetChanged()
        }
    }

    private fun showAddress() {
        if(hasAddress) {
            FragmentDialogShowAddress.newInstance()
                .show(childFragmentManager, "dialog")
        }
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
            val addressInfo = data.extras?.getParcelable<InfoAddress?>("address")
            val addressInfoStr = "${addressInfo?.nameCustomer} - ${addressInfo?.addressInfo} - ${addressInfo?.phoneCustomer}"
            txtAddressOrder.setText(addressInfoStr)
            hasAddress = true
            context?.getSharedPreferences("address" , Context.MODE_PRIVATE)?.
                edit()?.putString("addressInfo" , addressInfoStr)?.apply()
        }

        if(requestCode == REQUEST_CART && resultCode == Activity.RESULT_OK) {
            // showing snack bar with Undo option
            val snackbar = Snackbar.make(layoutOrder , getString(R.string.add_order_success), Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.go_to_history_order, object : View.OnClickListener {
                override fun onClick(view: View) {
                    startActivity(Intent(context , OrderHistoryActivity::class.java))
                }
            })
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }

        if(REQUEST_CHECK_SETTINGS == requestCode){
            if(resultCode == Activity.RESULT_OK) {
                Log.d("checkGPS" , "Ok")
                checkGPS(true)
            }

            if(resultCode == Activity.RESULT_CANCELED) {
//                preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
//                Toast.makeText(context , R.string.not_my_location , Toast.LENGTH_LONG).show()
//                swipeRefreshLocation.visibility = View.GONE
            }
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

    fun openDialogPermisstion(){
        val alert = AlertDialog.Builder(context!!)
        alert.setTitle(R.string.noti)
        alert.setMessage(R.string.noti_location_permisstion)
        alert.setPositiveButton(R.string.settings , { dialog, which ->
            openSetting()
        })
        alert.setNegativeButton(R.string.close , { dialog, which ->

        })
        alert.show()
    }

    fun openSetting(){
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.getPackageName(), null)
        intent.setData(uri)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.size < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            openDialogPermisstion()
            preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
            swipeRefreshLocation.visibility = View.GONE
            return
        }
        when(requestCode){
            REQUEST_FINE_LOCATION ->{
                preFragOrder.getLocation()
            }
        }
    }

    override fun successGetLocationAddress(address: String, latLng: com.google.android.gms.maps.model.LatLng) {
        this.latLngNow = latLng
        Log.d("checkGPS" , "dô 2")
        if(!address.equals("null") && latLngNow != null){
            val addressInfo = "${customer!!.nameCustomer} - $address - ${customer!!.phoneNumber ?: "0"}"
            txtAddressOrder.setText(addressInfo)
            context?.getSharedPreferences("address" , Context.MODE_PRIVATE)?.
                edit()?.putString("addressInfo" , addressInfo)?.apply()
        }
        else{
            Toast.makeText(context , R.string.not_my_location , Toast.LENGTH_LONG).show()
            preFragOrder.getAddressInfo(MainActivity.customer!!.idCustomer)
        }

        preFragOrder.removeRequest()
        swipeRefreshLocation.isRefreshing = false
        swipeRefreshLocation.visibility = View.GONE
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
        swipeRefreshLocation.isRefreshing = false
        swipeRefreshLocation.visibility = View.GONE
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

    fun callVisibleNavigationMain(boolean: Boolean){
        scrollDowm = !boolean
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

    fun setDiscount(discount: Discount?) {
        this.discount = discount
    }

    fun finishLoader(){
        lnLoader.visibility = View.GONE
    }

    override fun resultGetProductsBestBuy(arrProduct: ArrayList<Product>?) {}
    override fun resultGetProductsByCatalogy(arrProduct: ArrayList<Product>?) {}
    override fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?) {}
}