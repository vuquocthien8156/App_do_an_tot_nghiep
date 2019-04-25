package com.example.qthien.t__t.view.delivery_address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.qthien.t__t.model.Place
import com.example.qthien.t__t.presenter.pre_search_delivery.PreSearchDelivery
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_search_deliverry_address.*


class SearchDeliverryAddressActivity : AppCompatActivity() , IViewSearchDelivery {

    val PLACE_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_search_deliverry_address)
        setSupportActionBar(toolbarSearchDelivery)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)

        edtSearchPlace.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                PreSearchDelivery(this , this).searchPlace(edtSearchPlace.text.toString())
            }
            true
        }

        btnSelectOnMap.setOnClickListener({
            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this , data!!)
                val toastMsg = String.format("Place: %s", place.name)
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun successSearchPlace(arrPlace: ArrayList<Place>) {

    }

    override fun failureSearchPlace(message: String) {
    }
}
