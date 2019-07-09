package com.example.qthien.t__t

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.dialog_show_address.view.*

class FragmentDialogShowAddress : DialogFragment() {

    companion object {
        fun newInstance(): FragmentDialogShowAddress {
            val bundle = Bundle()
            val f = FragmentDialogShowAddress()
            f.arguments = bundle
            return f
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_show_address, container, false)

        val arrInfo =
            context?.getSharedPreferences("address", Activity.MODE_PRIVATE)?.getString("addressInfo", "")?.split("-")

        if (arrInfo != null) {
            val text = " Tên : ${arrInfo.get(0)} " +
                    "\n Số điện thoại : ${arrInfo.get(2)} " +
                    "\n Địa chỉ : ${arrInfo.get(1)}"
            Log.d("infoAddress", text)
            view.txtShowAddresss.text = text
        }

        return view
    }
}