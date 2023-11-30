package com.example.fridgestock.alarm

import java.time.LocalDateTime

data class AlarmItem(
    val id : Int,
    val alarmTime : LocalDateTime,
    val message : String
)
