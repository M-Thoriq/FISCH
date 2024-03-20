package com.harissabil.fisch.data

import com.harissabil.fisch.data.local.entity.FishEntity
import com.harissabil.fisch.data.local.room.FishDao
import com.harissabil.fisch.data.remote.api.ApiService

class FishRepository private constructor(
    private val apiService: ApiService,
    private val mFishDao: FishDao,
) {
    // Network
    suspend fun getWeather(lat: Double, lon: Double, currentWeather: Boolean) =
        apiService.getWeather(lat, lon, currentWeather)

    // Database
    suspend fun insert(fish: FishEntity) = mFishDao.insert(fish)

    suspend fun update(fish: FishEntity) = mFishDao.update(fish)

    suspend fun delete(fish: FishEntity) = mFishDao.delete(fish)

    fun getAllFish() = mFishDao.getAllFish()

    fun getFishByDate(date: String) = mFishDao.getFishByDate(date)

    fun getPrice() = mFishDao.getPrice()

    fun getWeight() = mFishDao.getWeight()

    companion object {
        @Volatile
        private var instance: FishRepository? = null
        fun getInstance(
            apiService: ApiService,
            fishDao: FishDao
        ): FishRepository =
            instance ?: synchronized(this) {
                instance ?: FishRepository(apiService, fishDao)
            }.also { instance = it }
    }
}