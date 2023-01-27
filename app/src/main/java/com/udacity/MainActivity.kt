package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*



class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var url : String = ""
    private var fileName: String = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

         downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        createChannel(
           CHANNEL_ID,
            CHANNEL_NAME
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radio_group.setOnCheckedChangeListener { _, _ ->
            when(radio_group.checkedRadioButtonId){
                R.id.radio_glide -> {
                    url = GLIDE_URL
                    fileName = getString(R.string.glide_repository)
                }

                R.id.radio_udacity -> {
                    url = UDACITY_PROJECT_URL
                    fileName = getString(R.string.udacity_repository)
                }

                R.id.radio_retrofit -> {
                    url = RETROFIT_URL
                    fileName = getString(R.string.retrofit_repository)
                }

                else -> {
                    return@setOnCheckedChangeListener
                }
            }
        }


        custom_button.setOnClickListener {
            if(radio_group.checkedRadioButtonId == -1){
                Toast.makeText(this, "please select ", Toast.LENGTH_SHORT).show()
            }else {
                download()
            }


        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE && downloadID == id) {
                custom_button.setState(ButtonState.Completed)

                notificationManager.sendNotification( getString(R.string.notification_body),context!!, fileName,
                        getDownloadStatus
                (id!!,downloadManager))
                Toast.makeText(context, getString(R.string.notification_body), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getDownloadStatus(id: Long, downloadManager: DownloadManager): String {
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor = downloadManager.query(query)
        var status = -1
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        }
        cursor.close()
        return when (status) {

            DownloadManager.STATUS_SUCCESSFUL -> {
                this.getString(R.string.download_success)
            }
            else -> {
                this.getString(R.string.download_fail)
            }
        }
    }

    private fun download() {

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        notificationManager.cancelNotifications()
        custom_button.setState(ButtonState.Loading)


    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

             notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }



    companion object {
        const val GLIDE_URL = "https://github.com/bumptech/glide"
        const val UDACITY_PROJECT_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        const val RETROFIT_URL = "https://github.com/square/retrofit"
        const val FILE_NAME = "FILE_NAME"
        const val DOWNLOAD_STATUS = "DOWNLOAD_STATUS"
         const val CHANNEL_ID = "udacity_channel"
        const val CHANNEL_NAME = "udacity"
    }

}
