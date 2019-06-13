package com.example.qthien.t__t.view.delivery_address

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sheet_bottom_add_address.*


class ModalBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(com.example.qthien.t__t.R.layout.sheet_bottom_add_address, container, false)



    override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
        val bottomSheetDialog= super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setOnShowListener(object : DialogInterface.OnShowListener{
            override fun onShow(dialog: DialogInterface?) {
                BottomSheetBehavior.from(standardBottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(standardBottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(standardBottomSheet).setHideable(true);
            }

        })
        return bottomSheetDialog;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING")
                    BottomSheetBehavior.STATE_SETTLING -> Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING")
                    BottomSheetBehavior.STATE_EXPANDED -> Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED")
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                Log.i("BottomSheetCallback", "slideOffset: $slideOffset")
//                val drawer = getSupportFragmentManager().getBackStackEntryCount() === 0
//                ObjectAnimator.ofFloat(drawerArrow, "progress", if (drawer) 0 else 1).start()
            }
        })
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}