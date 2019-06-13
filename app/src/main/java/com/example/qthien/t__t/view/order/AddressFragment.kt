package com.example.qthien.t__t.view.order

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R

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
        (context as OrderActivity).setTitle(R.string.address_deivery)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_address , container , false)
    }
}