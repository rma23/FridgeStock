package com.example.fridgestock.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var amount: String,
    var purchaseDate: String,
    var expirationDate: String,
    var description: String,
    var addedDate: String,
    var scheduleAlarm: Boolean,
)
