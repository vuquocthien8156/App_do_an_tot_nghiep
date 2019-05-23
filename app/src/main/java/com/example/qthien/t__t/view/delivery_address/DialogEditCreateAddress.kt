package com.example.qthien.t__t.view.delivery_address

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R

class DialogEditCreateAddress : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.setTitle(context?.getString(R.string.info_delivery))
        return layoutInflater.inflate(R.layout.dialog_edit_create_address , container , false)
    }
}