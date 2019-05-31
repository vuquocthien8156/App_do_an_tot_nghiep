package com.example.qthien.t__t

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho


class MyApplication : Application() {

    companion object {
        var instance : MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        instance = this
     }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

    fun getInstance() : MyApplication?{
        return instance
    }
}