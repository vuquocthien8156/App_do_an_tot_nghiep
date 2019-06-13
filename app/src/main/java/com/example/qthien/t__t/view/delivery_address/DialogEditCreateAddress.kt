package com.example.qthien.t__t.view.delivery_address

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.presenter.pre_address_delivery.PreAddEditAddressDialog
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.dialog_edit_create_address.*

class DialogEditCreateAddress : DialogFragment() , IDialogEditCreateAddress {

    interface AddressCommunication{
        fun successEditCreate()
    }

    var addressCommunication : AddressCommunication? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is AddressCommunication)
            addressCommunication = context
    }

    override fun failureEditCreateAddress(message: String) {
        Toast.makeText(context , R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    override fun resultEditOrCreateAddress(status: String?) {
        Toast.makeText(context , status , Toast.LENGTH_LONG).show()
        if(status.equals("Success")){
            if(addressCommunication != null) {
                addressCommunication?.successEditCreate()
                dismiss()
            }
        }
        else
            Toast.makeText(context , R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    var infoAddress : InfoAddress? = null
    var isCheckedd = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.setTitle(context?.getString(R.string.info_delivery))
        infoAddress = arguments?.get("address") as InfoAddress?
        return layoutInflater.inflate(R.layout.dialog_edit_create_address , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(infoAddress != null){
            edtNameCustomer.setText(infoAddress?.nameCustomer)
            edtPhone.setText(infoAddress?.phoneCustomer)
            edtAddress.setText(infoAddress?.addressInfo)

            if(infoAddress?.main == 1){
                switchDefault.isChecked = true
                isCheckedd = true
                switchDefault.isEnabled = false
            }
            else
                isCheckedd = false
        }

        switchDefault.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView : CompoundButton, isChecked : Boolean) {
                isCheckedd = isChecked;
            }
        });

        btnLuu.setOnClickListener({
            val name = edtNameCustomer.text.toString()
            val phone = edtPhone.text.toString()
            val address = edtAddress.text.toString()
            var main = if(isCheckedd) 1 else 0
            if(main == infoAddress?.main)
                main = -1

            if(!name.equals("") && !phone.equals("") && !address.equals("")){
                if(infoAddress != null){
                    infoAddress?.nameCustomer = name
                    infoAddress?.phoneCustomer = phone
                    infoAddress?.addressInfo = address
                    infoAddress?.main = main
                    Log.d("mainnnnnnnnn" , main.toString())
                    PreAddEditAddressDialog(this).updateAddressInfo(infoAddress!!)
                }
                else{
                    val infoAddressAdd = InfoAddress(0 ,name , phone , address , if(isCheckedd) 1 else 0, MainActivity.customer!!.idCustomer)
                    PreAddEditAddressDialog(this).addAddressInfo(infoAddressAdd)
                }
            }
            else
                Toast.makeText(context , R.string.please_enter_full_info , Toast.LENGTH_LONG).show()
        })
    }
}