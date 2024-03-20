package com.harissabil.fisch.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.harissabil.fisch.data.local.entity.FishEntity

@Dao
interface FishDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fish: FishEntity)

    @Update
    suspend fun update(fish: FishEntity)

    @Delete
    suspend fun delete(fish: FishEntity)

    @Query("SELECT * from fish ORDER BY id DESC")
    fun getAllFish(): LiveData<List<FishEntity>>

    @Query("SELECT * from fish WHERE date = :date ORDER BY id DESC")
    fun getFishByDate(date: String): LiveData<List<FishEntity>>

    @Query("SELECT price from fish")
    fun getPrice(): LiveData<List<Int>>

    @Query("SELECT weight from fish")
    fun getWeight(): LiveData<List<Int>>
}