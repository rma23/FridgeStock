package com.example.fridgestock.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("YYYY年MM月dd日")
    return formatter.format(Date(millis))
}

fun convertMillisToDateTime(millis: Long): String {
    val formatter = SimpleDateFormat("YYYY年MM月dd日:HH:mm:ss")
    return formatter.format(Date(millis))
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertStringToLocalDateTime(dateString: String): LocalDateTime {
    val replacedDate = dateString
        .replace(Regex("[年月]"), "-").replace(Regex("日"), "")

    val date = LocalDate.parse(replacedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    return date.atStartOfDay()
}