package com.example.qthien.t__t

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_bottom_gender.*

class FragmentChosseGenderBottom() : BottomSheetDialogFragment() , View.OnClickListener {

    var fragmentChosseGenderBottom : FragmentBottomGenderListener? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentBottomGenderListener){
            fragmentChosseGenderBottom = context
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.txtMan)
        {
            fragmentChosseGenderBottom?.resultChosseGender(true)
            dismiss()
        }
        else{
            fragmentChosseGenderBottom?.resultChosseGender(false)
            dismiss()
        }
    }

    interface FragmentBottomGenderListener{
        fun resultChosseGender(b : Boolean)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_bottom_gender , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtMan.setOnClickListener(this)
        txtWoman.setOnClickListener(this)
        txtClose.setOnClickListener { dismiss() }
    }
}