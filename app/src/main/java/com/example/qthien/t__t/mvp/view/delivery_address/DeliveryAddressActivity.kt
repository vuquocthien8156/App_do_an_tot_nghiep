package com.example.qthien.t__t.mvp.view.delivery_address

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.RecyclerItemTouchHelper
import com.example.qthien.t__t.RecyclerTouchListener
import com.example.qthien.t__t.adapter.DeliveryAdapterAddress
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.mvp.presenter.pre_address_delivery.PreAddEditAddressDialog
import com.example.qthien.t__t.mvp.presenter.pre_address_delivery.PreDelivertyAddressActi
import com.example.qthien.t__t.mvp.view.main.MainActivity
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import kotlinx.android.synthetic.main.activity_delivery_address.*
import kotlinx.android.synthetic.main.sheet_bottom_add_address.*


class DeliveryAddressActivity : AppCompatActivity() , IDeliveryAddressActi ,
        IDialogEditCreateAddress,  RecyclerItemTouchHelper.RecyclerItemTouchHelperListener , View.OnClickListener{

    override fun failureDeliveryAddress(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun failureEditCreateAddress(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun resultGetAllAddressInfoUser(arrrAddressInfo: ArrayList<InfoAddress>) {

        if(arrrAddressInfo.size > 0){
            arrAddress.clear()
            arrAddress.addAll(arrrAddressInfo)
            if(arrrAddressInfo.size > 1) {
                var a = -1
                for (i in 0 until arrAddress.size)
                    if (arrAddress[i].main == 1)
                        a = i
                val ad = arrAddress[a]
                arrAddress.removeAt(a)
                arrAddress.add(0, ad)
            }
            adapter.notifyDataSetChanged()
            relaOrder.visibility = View.GONE
        }
        else
            relaOrder.visibility = View.VISIBLE


        swipeRefreshLayout.isRefreshing = false
    }

    override fun resultEditOrCreateAddress(status: String?) {
        if(status.equals("Success")){

            if(type.equals("Edit")){
                PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)
                Toast.makeText(this, com.example.qthien.t__t.R.string.update_info_success, Toast.LENGTH_LONG).show()
                clearDataSheet()
                type = ""
            }
            else
                if(type.equals("Create")){
                    relaOrder.visibility = View.GONE
                    PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)
                    Toast.makeText(this, com.example.qthien.t__t.R.string.create_info_success, Toast.LENGTH_LONG).show()
                    clearDataSheet()
                    type = ""
                }
                else{
                    if(type.equals("Undo")){
                        arrAddress.add(positionSelected , addressUndo!!)
                        positionSelected = -1
                        addressUndo = null
                        type = ""
                    }
                    else {
                        arrAddress.removeAt(positionSelected)
                        if (arrAddress.size == 0)
                            relaOrder.visibility = View.VISIBLE

                        // showing snack bar with Undo option
                        val snackbar = Snackbar.make(layoutAddressActi, getString(R.string.you_delete_item), Snackbar.LENGTH_LONG)
                        snackbar.setAction(com.example.qthien.t__t.R.string.restore, object : View.OnClickListener {
                            override fun onClick(view: View) {
                                type = "Undo"
                                addressUndo?.da_xoa = 0
                                PreAddEditAddressDialog(this@DeliveryAddressActivity)
                                        .updateAddressInfo(addressUndo!!)
                            }
                        })
                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()
                    }
                }

            adapter.notifyDataSetChanged()
        }
        else {
            Toast.makeText(this, com.example.qthien.t__t.R.string.fail_again, Toast.LENGTH_LONG).show()
        }
    }

    lateinit var adapter: DeliveryAdapterAddress
    lateinit var arrAddress: ArrayList<InfoAddress>
    private var touchListener: RecyclerTouchListener? = null
    private var addressEdit : InfoAddress? = null
    var positionSelected = -1
    var type = "Edit"
    var addressUndo : InfoAddress? = null
    lateinit var bottomSheetBehavior : BottomSheetBehavior<View>
    val APP_REQUEST_CODE = 1
    val REQUEST_CODE_SEARCH_ADDRESS = 2
    val REQUEST_CODE_PICK_CONTACT = 3
    val REQUEST_READ_CONTACTS = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_delivery_address)

        setSupportActionBar(toolbarDeliveryAddress)
        toolbarDeliveryAddress.setTitleTextColor(Color.WHITE)
        supportActionBar?.setTitle(com.example.qthien.t__t.R.string.book_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)

        arrAddress = ArrayList()

        adapter = DeliveryAdapterAddress(this, arrAddress)
        recyDeliveryAddress.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyDeliveryAddress.adapter = adapter

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT  , this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyDeliveryAddress)

        swipeRefreshLayout.isRefreshing = true
        PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)

        swipeRefreshLayout.setOnRefreshListener {
            PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)
        }

        btnContinuteAdd.setOnClickListener({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.setPeekHeight(500)
        })

        initSheetBottom()
    }

    fun clearDataSheet(){
        edtNameCus.setText("")
        edtPhoneNumber.setText("")
        edtAddress.setText("")
        checkboxDefaultAddress.isChecked = false

        addressEdit = null

        bottomSheetBehavior.setPeekHeight(0)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun setUpDataSheet(position : Int){
        val add = arrAddress.get(position)
        addressEdit = add.copy()
        edtNameCus.setText(add.nameCustomer)
        edtNameCus.setSelection(edtNameCus.text.length)
        edtPhoneNumber.setText(add.phoneCustomer)
        edtAddress.setText(add.addressInfo)
        if(add.main == 1) {
            checkboxDefaultAddress.isChecked = true
        }
        checkboxDefaultAddress.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(add.main == 1)
                    checkboxDefaultAddress.isChecked = true
            }
        })
    }

    fun initSheetBottom(){
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutSheet)
        bottomSheetBehavior.setPeekHeight(0)

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> toolbarDeliveryAddress.setVisibility(View.GONE)

                    BottomSheetBehavior.STATE_COLLAPSED -> toolbarDeliveryAddress.setVisibility(View.VISIBLE)
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        ibtnCloseSheetBottomAdd.setOnClickListener(this)
        edtPhoneNumber.setOnClickListener(this)
        edtNameCus.setOnClickListener(this)
        ibtnSave.setOnClickListener(this)
        ibtnSearchPlace.setOnClickListener(this)
        ibtnPhoneBook.setOnClickListener(this)

        edtNameCus.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    bottomSheetBehavior.setPeekHeight(700)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        edtAddress.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    bottomSheetBehavior.setPeekHeight(700)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.edtNameCus->{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            R.id.ibtnCloseSheetBottomAdd ->{
                clearDataSheet()
                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }

            R.id.ibtnSearchPlace ->{
                val i = Intent(this , SearchDeliverryAddressActivity::class.java)
                startActivityForResult(i , REQUEST_CODE_SEARCH_ADDRESS)
            }
            R.id.edtPhoneNumber->{
                verifiPhone(edtPhoneNumber.text.toString())
            }

            R.id.ibtnPhoneBook -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(
                                arrayOf(Manifest.permission.READ_CONTACTS),
                                REQUEST_READ_CONTACTS)
                    else
                        startIntentContact()
                }
                else
                    startIntentContact()
            }
            R.id.ibtnSave -> {
                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)

                val name = edtNameCus.text.toString()
                val phone = edtPhoneNumber.text.toString()
                val address = edtAddress.text.toString()
                var main = if(checkboxDefaultAddress.isChecked) 1 else 0
                if(main == addressEdit?.main)
                    main = -1

                if(!name.equals("") && !phone.equals("") && !address.equals("")){
                    if(addressEdit != null){
                        addressEdit?.nameCustomer = name
                        addressEdit?.phoneCustomer = phone
                        addressEdit?.addressInfo = address
                        addressEdit?.main = main
                        type = "Edit"
                        PreAddEditAddressDialog(this).updateAddressInfo(addressEdit!!)
                    }
                    else{
                        type = "Create"
                        val infoAddressAdd = InfoAddress(0 ,name , phone , address , if(arrAddress.size == 0) 1 else main, MainActivity.customer!!.idCustomer)
                        arrAddress.add(infoAddressAdd)
                        PreAddEditAddressDialog(this).addAddressInfo(infoAddressAdd)
                    }
                }
                else
                    Toast.makeText(this , R.string.please_enter_full_info , Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openDialogPermisstion(){
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.noti)
        alert.setMessage(R.string.noti_contact_permisstion)
        alert.setPositiveButton(R.string.settings , { dialog, which ->
            openSetting()
        })
        alert.setNegativeButton(R.string.close , { dialog, which -> })
        alert.show()
    }

    fun openSetting(){
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", getPackageName(), null)
        intent.setData(uri)
        startActivity(intent)
    }

    fun startIntentContact(){
        val pickContact =  Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
        startActivityForResult(pickContact, REQUEST_CODE_PICK_CONTACT)
    }

    private fun verifiPhone(phone : String) {
        val intent = Intent(this , AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN   // or .ResponseTyadditionalpe.CODE
        )
        configurationBuilder.setInitialPhoneNumber(PhoneNumber("+84" , phone.replaceFirst("0" , "") , "VNM"))
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build()
        )
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (direction == ItemTouchHelper.LEFT){
            type = "Delelte"
            positionSelected = position
            addressUndo = arrAddress[position].copy()
            addressUndo?.da_xoa = 1
            PreAddEditAddressDialog(this@DeliveryAddressActivity)
                    .updateAddressInfo(addressUndo!!)
        }
        else{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.setPeekHeight(500)
            positionSelected = position
            adapter.notifyItemChanged(position)
            setUpDataSheet(position)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            val loginResult = data?.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
            var toastMessage = ""
            if(loginResult != null){
                if (loginResult.error != null) {
                    toastMessage = loginResult.error!!.errorType.message
//                    showErrorActivity(loginResult.error!!)
                } else if (loginResult.wasCancelled()) {
                    toastMessage = "Login Cancelled"
                } else {
                    if (loginResult.accessToken != null) {
                        toastMessage = "Success "
                    } else {
                        toastMessage = String.format(
                                "Successsss:%s...",
                                loginResult.authorizationCode!!.substring(0, 10)
                        )
                    }
                    getPhoneNumber()
                    AccountKit.logOut()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }

        if(requestCode == REQUEST_CODE_SEARCH_ADDRESS && resultCode == Activity.RESULT_OK && data != null){
            val address = data.extras?.getString("full_address")
//            Log.d("adddđressss" , address)
            edtAddress.setText(address)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        if(requestCode == REQUEST_CODE_PICK_CONTACT && resultCode == Activity.RESULT_OK && data != null){
            if (resultCode == Activity.RESULT_OK) {
                val contactData = data.getData()
                if(contactData != null){
                    val c = getContentResolver().query(contactData, null, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        val hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        var num = ""
                        if (Integer.valueOf(hasNumber) == 1) {
                            val numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers != null && numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                edtPhoneNumber.setText(num)
                            }
                            numbers?.close()
                        }
                    }
                    c?.close()
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_READ_CONTACTS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startIntentContact()
            else
                openDialogPermisstion()
        }
    }

    private fun getPhoneNumber() {
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(p0: Account?) {
                Log.d("phoneess" , p0?.phoneNumber.toString())
                edtPhoneNumber.setText(p0?.phoneNumber.toString().replace("+84" , "0"))
            }
            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())
            }
        })
    }

    override fun onBackPressed() {
        if(bottomSheetBehavior.peekHeight != 0){
            clearDataSheet()
            type = ""
        }
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_address_activity , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == com.example.qthien.t__t.R.id.menu_add_address){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.setPeekHeight(500)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
