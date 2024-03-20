package com.harissabil.fisch.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponse(

	@field:SerializedName("elevation")
	val elevation: Double,

	@field:SerializedName("generationtime_ms")
	val generationtimeMs: Double,

	@field:SerializedName("timezone_abbreviation")
	val timezoneAbbreviation: String,

	@field:SerializedName("timezone")
	val timezone: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("utc_offset_seconds")
	val utcOffsetSeconds: Int,

	@field:SerializedName("current_weather")
	val currentWeather: CurrentWeather,

	@field:SerializedName("longitude")
	val longitude: Double
) : Parcelable

@Parcelize
data class CurrentWeather(

	@field:SerializedName("weathercode")
	val weathercode: Int,

	@field:SerializedName("temperature")
	val temperature: Double,

	@field:SerializedName("windspeed")
	val windspeed: Double,

	@field:SerializedName("is_day")
	val isDay: Int,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("winddirection")
	val winddirection: Double
) : Parcelable
