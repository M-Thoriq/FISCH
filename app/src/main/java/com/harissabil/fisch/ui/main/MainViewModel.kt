package com.harissabil.fisch.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.harissabil.fisch.data.FishRepository
import com.harissabil.fisch.data.remote.response.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: FishRepository) : ViewModel() {

    private val _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse

    fun getWeather(lat: Double, lon: Double, currentWeather: Boolean) {
        viewModelScope.launch {
            try {
                val response = repository.getWeather(lat, lon, currentWeather)
                _weatherResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, WeatherResponse::class.java)
                Log.e(TAG, "getWeather: $errorBody")
                e.printStackTrace()
            }
        }
    }

    fun getPrice() = repository.getPrice()

    fun getWeight() = repository.getWeight()

    fun getAllFish() = repository.getAllFish()

    fun getFishByDate(date: String) = repository.getFishByDate(date)

    companion object {
        private const val TAG = "MainViewModel"
    }
}