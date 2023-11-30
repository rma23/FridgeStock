package com.example.fridgestock

import android.content.Context
import androidx.room.Room
import com.example.fridgestock.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "food_database").build()

    @Provides
    fun provideDao(db: AppDatabase) = db.foodDao()
}