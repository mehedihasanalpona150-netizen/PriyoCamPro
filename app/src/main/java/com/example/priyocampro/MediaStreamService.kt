package com.example.priyocampro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MediaStreamService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val videoUri = intent?.getStringExtra("VIDEO_URI")
        val rotation = intent?.getIntExtra("ROTATION", 0) ?: 0
        val isFlipped = intent?.getBooleanExtra("FLIP", false) ?: false

        when (action) {
            "ACTION_START_STREAM" -> {
                // মিডিয়া স্ট্রিমিং ও ক্যামেরা ইঞ্জিনে ফ্রেম প্রসেসিং লজিক
            }
            "ACTION_STOP_STREAM" -> {
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startForegroundServiceNotification() {
        val channelId = "priyo_cam_pro_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "PriyoCamPro Virtual Stream",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("PriyoCamPro Active")
            .setContentText("Virtual Camera Service is running...")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .build()

        startForeground(101, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
