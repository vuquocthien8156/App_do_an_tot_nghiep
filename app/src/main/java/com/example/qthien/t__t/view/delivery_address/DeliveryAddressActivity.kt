package com.example.qthien.t__t.view.delivery_address

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.DeliveryAdapterAddress
import com.example.qthien.t__t.model.AddressDelivery
import kotlinx.android.synthetic.main.activity_delivery_address.*

class DeliveryAddressActivity : AppCompatActivity() , DeliveryAdapterAddress.AddressCommunication {

    lateinit var adapter : DeliveryAdapterAddress
    lateinit var arrAddress : ArrayList<AddressDelivery>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_address)

        setSupportActionBar(toolbarDeliveryAddress)
        toolbarDeliveryAddress.setTitleTextColor(Color.WHITE)
        supportActionBar?.setTitle(R.string.book_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        arrAddress = ArrayList()
        arrAddress.add(AddressDelivery(1 , "Vũ Quốc Thiên" , "45 Đường 3 , Tam Bình , Thủ Đức" , "84782328156" , 1 , 0))
        arrAddress.add(AddressDelivery(2 , "Đặng Nguyễn Xuân Nhung" , "50 Đường 7 , Tam Phú , Thủ Đức" , "84763197311" , 0 , 0))
        arrAddress.add(AddressDelivery(3 , "Ngô Ngọc Lễ" , "57 Phú Châu , Tam Phú , Thủ Đứcccccccccccccccccc" , "123456789" , 0 , 0))


        adapter = DeliveryAdapterAddress(this , arrAddress)
        recyDeliveryAddress.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyDeliveryAddress.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun selectAddress(position: Int) {
    }

    override fun remove(position: Int) {
    }

    override fun edit(position: Int) {
        val dialog = DialogEditCreateAddress()
        val bundle = Bundle()
        bundle.putParcelable("address" , arrAddress.get(position))
        dialog.show(supportFragmentManager , "dialog")
    }
}
