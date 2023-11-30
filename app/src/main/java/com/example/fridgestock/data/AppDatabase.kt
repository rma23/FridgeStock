package com.example.fridgestock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fridgestock.components.DateTimeConverter

@Database(entities = [Food::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}