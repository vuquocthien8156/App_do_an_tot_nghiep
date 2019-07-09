package com.example.qthien.t__t

import android.content.ContentResolver
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val notification = NotificationCompat.Builder(this , "M_CH_ID")
            .setContentTitle(remoteMessage!!.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" + getPackageName()+"/"+ R.raw.sound_noti))
            .build()
        val manager = NotificationManagerCompat.from(applicationContext)
        manager.notify(123, notification)
    }
}