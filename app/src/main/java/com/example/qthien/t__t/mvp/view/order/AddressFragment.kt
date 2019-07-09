package com.example.qthien.t__t.mvp.view.order

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.mvp.view.delivery_address.DeliveryAddressActivity
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import kotlinx.android.synthetic.main.fragment_address.*
import java.util.regex.Pattern


class AddressFragment : Fragment(){

    companion object {
        fun newInstance() : AddressFragment {
            val f = AddressFragment()
            val bundle = Bundle()
            bundle.putString("fragment" , "Address")
            f.arguments = bundle
            return f
        }
    }

    override fun onResume() {
        super.onResume()
        (context as OrderActivity).setTitle(com.example.qthien.t__t.R.string.info_delivery)
    }

    val REQUEST_CODE_ADDRESS = 1
    val REQUEST_CODE_PICK_CONTACT= 2
    var callActivity : FragmentOrderCallActivi? = null
    val REQUEST_READ_CONTACTS = 3

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentOrderCallActivi)
            callActivity = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(com.example.qthien.t__t.R.layout.fragment_address , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val strInfo = context?.getSharedPreferences("address", Activity.MODE_PRIVATE)?.getString("addressInfo", null)

        if(strInfo != null){
            val arrInfo = strInfo.split("-")
            edtName.setText(arrInfo.get(0).trim())
            edtName.setSelection(arrInfo.get(0).trim().length)
            edtPhone.setText(arrInfo.get(2).trim())
            edtAddress.setText(arrInfo.get(1).trim())
        }
        else{
            edtName.setText(customer?.nameCustomer ?: "")
            edtName.setSelection(customer?.nameCustomer?.length ?: 0)
            edtPhone.setText(customer?.phoneNumber)
        }

        ibtnSelectedContact.setOnClickListener({
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        REQUEST_READ_CONTACTS)
                else
                    startIntentContact()
            }
            else
                startIntentContact()
        })

        btnSelectedInBook.setOnClickListener({
            val i = Intent(context , DeliveryAddressActivity::class.java)
            startActivityForResult(i , REQUEST_CODE_ADDRESS)
        })
    }

    fun startIntentContact(){
        val pickContact =  Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContact, REQUEST_CODE_PICK_CONTACT)
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

    fun loadAnimEdt(idString :  Int, edt : EditText){
        Toast.makeText(context , idString , Toast.LENGTH_LONG).show()
        val shake = AnimationUtils.loadAnimation(context, com.example.qthien.t__t.R.anim.shake_edittext)
        edt.startAnimation(shake)
    }

    fun String.isPhoneNumber() : Boolean {
        val check : Boolean
        if(Pattern.matches("^[+]?[0-9]{9,11}$", this))
            check = true
        else
            check = false

        return check
    }

    fun openDialogPermisstion(){
        val alert = AlertDialog.Builder(context!!)
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
        val uri = Uri.fromParts("package", activity?.getPackageName(), null)
        intent.setData(uri)
        startActivity(intent)
    }

    fun checkValidation(){

        if(edtName.text.isNullOrEmpty()) {
            loadAnimEdt(com.example.qthien.t__t.R.string.error_name , edtName)
            return
        }

        Log.d("validate" , edtPhone.text.toString())
        Log.d("validate" , edtPhone.text.toString().isPhoneNumber().toString())
        if(!edtPhone.text.toString().isPhoneNumber()){
            loadAnimEdt(com.example.qthien.t__t.R.string.error_phone , edtPhone)
            return
        }

        if(edtAddress.text.isNullOrEmpty()){
            loadAnimEdt(com.example.qthien.t__t.R.string.error_address , edtAddress)
            return
        }

        val info = "${edtName.text} - ${edtPhone.text} - ${edtAddress.text}"
        callActivity?.addressValidation(info)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_ADDRESS && resultCode == Activity.RESULT_OK && data != null){
            val addressInfo = data.extras?.getParcelable<InfoAddress?>("address")

            if(addressInfo != null){
                edtName.setText(addressInfo.nameCustomer)
                edtName.setSelection(addressInfo.nameCustomer.length)
                edtPhone.setText(addressInfo.phoneCustomer)
                edtAddress.setText(addressInfo.addressInfo)
            }
        }

        if(requestCode == REQUEST_CODE_PICK_CONTACT && resultCode == Activity.RESULT_OK && data != null){
            if (resultCode == Activity.RESULT_OK) {
                val contactData = data.getData()
                if(contactData != null){
                    val c = context!!.getContentResolver().query(contactData, null, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        val hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        var num = ""
                        if (Integer.valueOf(hasNumber) == 1) {
                            val numbers = context!!.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers != null && numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                edtPhone.setText(num)
                            }
                            numbers?.close()
                        }
                    }
                    c?.close()
                }
            }
        }
    }
}