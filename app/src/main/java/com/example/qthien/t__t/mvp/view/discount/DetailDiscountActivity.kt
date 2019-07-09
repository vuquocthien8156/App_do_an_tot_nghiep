package com.example.qthien.t__t.mvp.view.discount

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.activity_detail_discount.*
import java.text.SimpleDateFormat
import java.util.*


class DetailDiscountActivity : AppCompatActivity() {

    var discount : Discount? = null
    var urlShare = ""
    var menuShare : MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_detail_discount)
        setSupportActionBar(toolbarDetailDiscount)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close_circle)
        toolbarDetailDiscount.setTitleTextColor(Color.WHITE)
        supportActionBar?.setTitle("")

        discount = intent.extras?.getParcelable("discount")

        urlShare = "${RetrofitInstance.baseUrl}/products/discount?id=${discount?.idDiscount}"

        initInfoDiscount()

        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collap.setTitle("Chi tiết khuyến mãi")
                    supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)
                    menuShare?.setIcon(R.drawable.ic_share_white_30dp)
                    collap.setCollapsedTitleTextColor(Color.WHITE)
                    btnUseDiscountNow.visibility = View.GONE
                    isShow = true
                } else if (isShow) {
                    collap.setTitle("")//careful there should a space between double quote otherwise it wont work
                    isShow = false
                    btnUseDiscountNow.visibility = View.VISIBLE
                    supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close_circle)
                    menuShare?.setIcon(R.drawable.ic_share_circle)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_law , menu)
        menuShare = menu?.findItem(R.id.menu_share_law)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == com.example.qthien.t__t.R.id.menu_share_law){
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, urlShare)
            startActivity(Intent.createChooser(i, "Share URL"))
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n", "CheckResult")
    fun initInfoDiscount(){
        txtNameDiscount.setText(discount?.nameDiscount)
        val sim = SimpleDateFormat("yyyy-MM-dd")
        val sim2 = SimpleDateFormat("dd-MM-yyyy")
        txtTimer.setText("${getString(com.example.qthien.t__t.R.string.duration)} ${ sim2.format(sim.parse(discount?.dateStart)) } ${getString(
            com.example.qthien.t__t.R.string.to)} ${sim2.format(sim.parse(discount?.dateEnd))}")
        txtDecripTion.setText(discount?.decripDiscount.toString())
        GlideApp.with(this).load("${RetrofitInstance.baseUrl}/${discount?.image}").into(imgDiscount)

        val calender = Calendar.getInstance()
        val dateNow = calender.time
        val dateEnd = SimpleDateFormat("yyyy-MM-dd").parse(discount?.dateEnd)

        val noOfDaysBetween = dateEnd.time - dateNow.time

        val seconds = noOfDaysBetween / 1000
        val minutes = seconds / 60
        var hours = minutes / 60
        if(minutes < 60)
            hours += 1

        if(hours > 0) {
            if (discount!!.limitCode > 0 && discount?.codeLeft == 0) {
                btnUseDiscountNow.setBackgroundResource(R.color.colorPrimary)
                btnUseDiscountNow.setText(R.string.use_full_limit)
            } else {
                btnUseDiscountNow.setBackgroundResource(R.color.colorAccent)
                btnUseDiscountNow.setText(R.string.use_now)

                btnUseDiscountNow.setOnClickListener({
                    val i = Intent()
                    i.putExtra("discount" , discount)
                    setResult(Activity.RESULT_OK , i)
                    finish()
                })
            }
        }
        else{
            btnUseDiscountNow.setBackgroundResource(R.color.colorPrimary)
            btnUseDiscountNow.setText(R.string.dealine)
            txtTimer.setTextColor(ContextCompat.getColor(this , R.color.colorPrimary))
        }
    }
}
