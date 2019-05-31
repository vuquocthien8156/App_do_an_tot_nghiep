package com.example.qthien.t__t.view.customer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.fragment_chosse_method_picture.*

class ChosseMethodPictureFragment() : DialogFragment() , View.OnClickListener{

    interface ChosseMethodListener{
        fun methodSelected(method : Int)
    }

    var chosseMethodListener : ChosseMethodListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is ChosseMethodListener)
            chosseMethodListener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_chosse_method_picture , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.setTitle(R.string.chosse_method)

        btnChossePicture.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.btnChossePicture){
            chosseMethodListener?.methodSelected(0)
        }
        else
            chosseMethodListener?.methodSelected(1)
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        getDialog().getWindow()?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}