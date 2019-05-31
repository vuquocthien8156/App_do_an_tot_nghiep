package com.example.qthien.t__t

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.qthien.t__t.view.main.MainActivity

class MyExceptionHandler(var context : Activity) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        val intent = Intent(context , MainActivity::class.java)
        intent.putExtra("crash" , true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                       Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK)

        val pen =  PendingIntent.getActivity(MyApplication.instance?.applicationContext , 0 , intent , PendingIntent.FLAG_ONE_SHOT)

        val alar = MyApplication.instance?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alar.set(AlarmManager.RTC , System.currentTimeMillis() + 1000 , pen)

        context.finish()
        System.exit(2)
    }
}