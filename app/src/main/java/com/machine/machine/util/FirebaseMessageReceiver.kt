package com.machine.machine.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.machine.machine.R
import com.machine.machine.ui.SplashActivity
import java.util.concurrent.atomic.AtomicInteger

class FirebaseMessageReceiver: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        getNotification(this,message)
    }

    private fun getNotification(context: Context, remoteMessage: RemoteMessage) {
        val channelId = "UpShop"
        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = Intent.ACTION_VIEW
        val pendingIntent: PendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(
                    applicationContext,
                    NotificationID.iD,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getActivity(
                    applicationContext,
                    NotificationID.iD,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

        val notificationBuilder = NotificationCompat.Builder(context.applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setLights(Color.BLUE, 500, 500)
            .setVibrate(longArrayOf(500, 500, 500))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)

        val notificationManager = NotificationManagerCompat.from(context)

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "UpShop",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NotificationID.iD, notificationBuilder.build())
    }
}

internal object NotificationID {
    private val c = AtomicInteger(100)
    val iD: Int
        get() = c.incrementAndGet()
}