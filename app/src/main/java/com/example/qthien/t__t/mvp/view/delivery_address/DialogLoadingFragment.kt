package com.example.qthien.t__t.mvp.view.delivery_address

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.qthien.t__t.R

class DialogLoadingFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return layoutInflater.inflate(R.layout.fragment_dialog_loading , container , false)
    }
}