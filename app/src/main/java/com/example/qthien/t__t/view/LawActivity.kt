package com.example.qthien.t__t.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_law.*



class LawActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_law)

        setSupportActionBar(toolbarLaw)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        webViewLaw.loadUrl("https://order.thecoffeehouse.com/help.html?fbclid=IwAR1ky7S7QZ9DYGXMh_chjqsGhAL2GWbBeq0evns3HQw3Ez2v8ySK0rL71N8")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_law , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == com.example.qthien.t__t.R.id.menu_share_law){
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, "https://order.thecoffeehouse.com/help.html?fbclid=IwAR1ky7S7QZ9DYGXMh_chjqsGhAL2GWbBeq0evns3HQw3Ez2v8ySK0rL71N8")
            startActivity(Intent.createChooser(i, "Share URL"))
        }
        return super.onOptionsItemSelected(item)
    }
}
