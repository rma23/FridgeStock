package com.example.fridgestock.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: Food): Long

    @Query("SELECT * FROM Food")
    fun loadAllFoods(): Flow<List<Food>>

    @Query("SELECT * FROM Food WHERE id = :foodId")
    fun loadFoodById(foodId: Int): Flow<Food>

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)
}