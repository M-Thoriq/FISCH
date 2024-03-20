package com.harissabil.fisch.di

import android.content.Context
import com.harissabil.fisch.data.FishRepository
import com.harissabil.fisch.data.local.room.FishDatabase
import com.harissabil.fisch.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): FishRepository {
        val apiService = ApiConfig.getApiService()
        val database = FishDatabase.getDatabase(context)
        val dao = database.fishDao()
        return FishRepository.getInstance(apiService, dao)
    }
}