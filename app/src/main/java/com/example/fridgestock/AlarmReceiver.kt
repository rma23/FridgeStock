package com.example.fridgestock

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: return

        val manager = NotificationManagerCompat.from(context)
        val channel = NotificationChannelCompat.Builder(
            "channel_id",
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
        )
            .setName("Notification from FridgeStock")
            .build()

        manager.createNotificationChannel(channel)

        val pendingIntent = PendingIntent.getActivity(
            context,
            context.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        context.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channel.id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FridgeStockからのお知らせ")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            notificationManager.notify(1, builder)

            Log.d("notificationTEST", "notification sent")
        }
    }
}