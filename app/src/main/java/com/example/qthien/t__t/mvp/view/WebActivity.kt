package com.example.qthien.t__t.mvp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_law.*









class WebActivity : AppCompatActivity() {

    var url : String? = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_law)

        setSupportActionBar(toolbarLaw)
        toolbarLaw.setTitleTextColor(Color.WHITE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        url = intent.extras?.getString("url")
        val title = intent.extras?.getString("title")

        if(title != null)
            setTitle(title)
        else
            supportActionBar?.setDisplayShowTitleEnabled(false)

        webViewLaw.getSettings().setJavaScriptEnabled(true)
        webViewLaw.getSettings().setLoadWithOverviewMode(true)
//        webViewLaw.getSettings().setUseWideViewPort(true)
        webViewLaw.getSettings().loadsImagesAutomatically = true
        webViewLaw.getSettings().domStorageEnabled = true
        webViewLaw.setWebViewClient(object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                progress.visibility = View.VISIBLE
                webViewLaw.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress.visibility = View.GONE
            }
        })

        webViewLaw.loadUrl(url)
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
            i.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(i, "Share URL"))
        }
        return super.onOptionsItemSelected(item)
    }
}
