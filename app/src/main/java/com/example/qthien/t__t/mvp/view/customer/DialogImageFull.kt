package com.example.qthien.t__t.mvp.view.customer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.layou_image_full_avata.view.*

class DialogImageFull : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.layou_image_full_avata , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val img  = view.imageViewFull

        var url = arguments?.getString("url")

        if(url != null && !url.contains("https://")) {
            GlideApp.with(this).load(RetrofitInstance.baseUrl + "/" + url)
                    .into(img)
        }else {
            url = "https://graph.facebook.com/${customer?.id_fb}/picture?height=1000&width=1000"
            GlideApp.with(this).load(url).into(img)
        }


    }

    override fun onResume() {
        super.onResume()
        val window = dialog.window
        window!!.decorView.setOnTouchListener(
            SwipeDismissTouchListener(
                window.decorView,
                null,
                object : SwipeDismissTouchListener.DismissCallbacks {
                    override fun canDismiss(token: Any?): Boolean {
                        return true
                    }

                    override fun onDismiss(view: View, token: Any?) {
                        dismiss()
                    }
                })
        )
    }
}