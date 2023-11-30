package com.example.fridgestock.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fridgestock.AlarmReceiver
import com.example.fridgestock.components.convertMillisToDateTime
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }
        val alarmTime = alarmItem.alarmTime.atZone(ZoneId.of("Asia/Tokyo")).toInstant().toEpochMilli()

        val alarmTimeTest =
            LocalDateTime.of(2023, 11, 30, 0, 45)


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                alarmItem.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )



        //val alarmTime2 = convertMillisToDate(alarmTime)
        val alarmTime2 = convertMillisToDateTime(alarmTime)

        Log.d("alarm", "TEST alarm # ${alarmItem.id} set at $alarmTime2")
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.d("unSetTEST", "alarm # {$alarmItem.id} unset")
    }
}