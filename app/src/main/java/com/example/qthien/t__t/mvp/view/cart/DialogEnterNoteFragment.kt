package com.example.qthien.t__t.mvp.view.cart

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.fragment_dialog_enter.view.*

class DialogEnterNoteFragment : DialogFragment(){

    interface TransmissionNoteInDialogToActivity{
        fun transmissionNote(note : String)
    }

    var transmission : TransmissionNoteInDialogToActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is TransmissionNoteInDialogToActivity)
            transmission = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = android.support.v7.app.AlertDialog.Builder(context!!)
        val view = activity!!.layoutInflater.inflate(R.layout.fragment_dialog_enter , null)
        builder.setView(view)

        view.edtEnterNote.setText(arguments?.getString("note"))
        view.edtEnterNote.requestFocus()
        builder.setPositiveButton(R.string.ok , object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val note = view.edtEnterNote.text.toString()
                transmission?.transmissionNote(note)
                dismiss()
            }
        })

        builder.setNegativeButton(R.string.cancel , object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dismiss()
            }

        })

        return builder.create()
    }
}