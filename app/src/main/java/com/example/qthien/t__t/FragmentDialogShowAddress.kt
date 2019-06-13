package com.example.qthien.t__t

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.qthien.t__t.model.InfoAddress
import kotlinx.android.synthetic.main.dialog_show_address.view.*

class FragmentDialogShowAddress : DialogFragment() {

    companion object {
        fun newInstance(address : InfoAddress?) : FragmentDialogShowAddress {
            val bundle = Bundle()
            bundle.putParcelable("address" , address)
            val f = FragmentDialogShowAddress()
            f.arguments = bundle
            return f
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_show_address , container , false)
        val infoAddress = arguments?.getParcelable<InfoAddress?>("address")
        Log.d("infoAddress" , infoAddress.toString())
        if(infoAddress?.nameCustomer.equals(""))
            view.txtShowAddresss.setText(infoAddress?.addressInfo)
        else{
            val text = " Tên : ${infoAddress?.nameCustomer} " +
                    "\n Số điện thoại : ${infoAddress?.phoneCustomer} " +
                    "\n Địa chỉ : ${infoAddress?.addressInfo}"
            Log.d("infoAddress" , text)
            view.txtShowAddresss.text = text
        }

        return view
    }
}