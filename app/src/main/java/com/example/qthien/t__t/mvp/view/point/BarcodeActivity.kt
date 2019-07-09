package com.example.qthien.t__t.mvp.view.point

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.qthien.t__t.R
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import kotlinx.android.synthetic.main.activity_barcode.*
import net.glxn.qrgen.android.QRCode

class BarcodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)

        setSupportActionBar(toolbarBarcode)
        supportActionBar?.setTitle(R.string.scan_id_user)
        toolbarBarcode.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val bitmap = QRCode.from(customer?.idCustomer.toString()).withSize(600, 600).bitmap()
        imgBarCode.setImageBitmap(bitmap)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
