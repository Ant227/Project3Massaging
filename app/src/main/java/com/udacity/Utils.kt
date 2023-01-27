package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity.Companion.CHANNEL_ID
import com.udacity.MainActivity.Companion.DOWNLOAD_STATUS
import com.udacity.MainActivity.Companion.FILE_NAME

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0



fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, fileName: String, downloadStatus: String) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    contentIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val detailContentIntent = Intent(applicationContext, DetailActivity::class.java)
        .apply {  putExtra(FILE_NAME, fileName)
            putExtra(DOWNLOAD_STATUS, downloadStatus) }


    val detailPendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        detailContentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)
    val detailAction = NotificationCompat.Action(null, applicationContext.getString(R.string.notification_detail_action), detailPendingIntent)

    val image = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setLargeIcon(image)
        .addAction(detailAction)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)


    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}
