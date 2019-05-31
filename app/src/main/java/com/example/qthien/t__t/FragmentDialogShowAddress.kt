package com.example.qthien.t__t

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.qthien.t__t.model.InfoAddress

class FragmentDialogShowAddress : DialogFragment() {

    companion object {
        fun newInstance(address : InfoAddress) : FragmentDialogShowAddress {
            val bundle = Bundle()
            bundle.putParcelable("address" , address)
            val f = FragmentDialogShowAddress()
            f.arguments = bundle
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_show_address , container , false)
        val info = arguments?.getParcelable<InfoAddress>("address")
        return view
    }
}