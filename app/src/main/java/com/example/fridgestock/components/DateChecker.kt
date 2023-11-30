package com.example.fridgestock.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun checkExpirationDate(expirationDate: String): Boolean {

    if (expirationDate == "") {
        return false
    } else {

        val replacedExpirationDate = expirationDate
            .replace(Regex("[年月]"), "-").replace(Regex("日"), "")

        val date =
            LocalDate.parse(replacedExpirationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        return date.isBefore(LocalDate.now())
    }
}
