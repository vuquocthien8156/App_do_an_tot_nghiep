package com.example.qthien.t__t.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.qthien.t__t.R
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

        val bitmap = QRCode.from("123456789").withSize(500, 500).bitmap()
        imgBarCode.setImageBitmap(bitmap)
    }
}
