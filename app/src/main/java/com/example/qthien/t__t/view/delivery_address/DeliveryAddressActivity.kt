package com.example.qthien.t__t.view.delivery_address

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.RecyclerItemTouchHelper
import com.example.qthien.t__t.RecyclerTouchListener
import com.example.qthien.t__t.adapter.DeliveryAdapterAddress
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.presenter.pre_address_delivery.PreAddEditAddressDialog
import com.example.qthien.t__t.presenter.pre_address_delivery.PreDelivertyAddressActi
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_delivery_address.*


class DeliveryAddressActivity : AppCompatActivity() , IDeliveryAddressActi ,
        IDialogEditCreateAddress , DialogEditCreateAddress.AddressCommunication
,  RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    override fun successEditCreate() {
        PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)
    }

    override fun failureDeliveryAddress(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun failureEditCreateAddress(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun resultGetAllAddressInfoUser(arrrAddressInfo: ArrayList<InfoAddress>) {

        if(arrrAddressInfo.size > 0){
            arrAddress.clear()
            arrAddress.addAll(arrrAddressInfo)

            var a = -1
            for(i in 0 until arrAddress.size)
                if(arrAddress[i].main == 1)
                    a = i
            val ad = arrAddress[a]
            arrAddress.removeAt(a)
            arrAddress.add(0 , ad)
            adapter.notifyDataSetChanged()
            relaOrder.visibility = View.GONE
        }
        else
            relaOrder.visibility = View.VISIBLE


        swipeRefreshLayout.isRefreshing = false
    }

    override fun resultEditOrCreateAddress(status: String?) {
        if(status.equals("Success")){

            if(arrAddress.size == 0)
                relaOrder.visibility = View.VISIBLE

        }
        else
            Toast.makeText(this , com.example.qthien.t__t.R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    lateinit var adapter: DeliveryAdapterAddress
    lateinit var arrAddress: ArrayList<InfoAddress>
    private var touchListener: RecyclerTouchListener? = null
    var positionSelected = -1
    var addressUndo : InfoAddress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_delivery_address)

        setSupportActionBar(toolbarDeliveryAddress)
        toolbarDeliveryAddress.setTitleTextColor(Color.WHITE)
        supportActionBar?.setTitle(com.example.qthien.t__t.R.string.book_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)

        arrAddress = ArrayList()

        adapter = DeliveryAdapterAddress(this, arrAddress)
        recyDeliveryAddress.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyDeliveryAddress.adapter = adapter

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT  , this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyDeliveryAddress)

        swipeRefreshLayout.isRefreshing = true
        PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)

        swipeRefreshLayout.setOnRefreshListener {
            PreDelivertyAddressActi(this).getAllAddressInfoUser(MainActivity.customer!!.idCustomer)
        }


        btnContinuteAdd.setOnClickListener({
            val dialog = DialogEditCreateAddress()
            dialog.show(supportFragmentManager, "dialog")
        })

//        setUpRecyclerViewSwipe()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (direction == ItemTouchHelper.LEFT){
            positionSelected = position
            addressUndo = arrAddress[position].copy()

            arrAddress.removeAt(position)
            adapter.notifyDataSetChanged()

            // showing snack bar with Undo option
            val snackbar = Snackbar.make(layoutAddressActi , getString(R.string.you_delete_item), Snackbar.LENGTH_LONG)
            snackbar.setAction(com.example.qthien.t__t.R.string.restore , object : View.OnClickListener {
                override fun onClick(view: View) {
                    arrAddress.add(positionSelected , addressUndo!!)
                    adapter.notifyDataSetChanged()
                }
            })
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar?, event: Int) {
                    super.onDismissed(snackbar, event)
                    if(event == DISMISS_EVENT_TIMEOUT) {
                        addressUndo?.da_xoa = 1
                        PreAddEditAddressDialog(this@DeliveryAddressActivity)
                                .updateAddressInfo(addressUndo!!)
                        Log.d("snackk", "dissmis")
                    }
                }
            })
            snackbar.show()
        }
        else{
            positionSelected = position
            val dialog = DialogEditCreateAddress()
            val bundle = Bundle()
            bundle.putParcelable("address", arrAddress.get(position))
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "dialog")
        }
    }


//    fun setUpRecyclerViewSwipe() {
//        touchListener = RecyclerTouchListener(this, recyDeliveryAddress)
//        touchListener?.setClickable(object : RecyclerTouchListener.OnRowClickListener {
//            override fun onRowClicked(position: Int) {
//                Toast.makeText(applicationContext, arrAddress.get(position).nameCustomer , Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onIndependentViewClicked(independentViewID: Int, position: Int) {
//
//            }
//        })
//
//        touchListener?.setSwipeOptionViews(com.example.qthien.t__t.R.id.delete_task, com.example.qthien.t__t.R.id.edit_task)
//        touchListener?.setSwipeable(
//                com.example.qthien.t__t.R.id.rowFG, com.example.qthien.t__t.R.id.rowBG,
//            object : RecyclerTouchListener.OnSwipeOptionsClickListener {
//                override fun onSwipeOptionClicked(viewID: Int, position: Int) {
//                    when (viewID) {
//                        com.example.qthien.t__t.R.id.delete_task -> {
//                            positionSelected = position
//                            arrAddress[position].da_xoa = 1
//                            PreAddEditAddressDialog(this@DeliveryAddressActivity)
//                                    .updateAddressInfo(arrAddress[position])
//                        }
//                        com.example.qthien.t__t.R.id.edit_task -> {
//                            val dialog = DialogEditCreateAddress()
//                            val bundle = Bundle()
//                            bundle.putParcelable("address", arrAddress.get(position))
//                            dialog.arguments = bundle
//                            dialog.show(supportFragmentManager, "dialog")
//                        }
//                    }
//                }
//
//            })
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_address_activity , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == com.example.qthien.t__t.R.id.menu_add_address){
            val dialog = DialogEditCreateAddress()
            dialog.show(supportFragmentManager, "dialog")
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyItemChanged(positionSelected)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
