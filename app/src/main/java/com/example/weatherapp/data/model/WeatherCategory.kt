package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherCategory(
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val name: String,
    )

data class Weather(
    val main: String,
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
)

data class Wind(
    val speed: Double
)

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val name: String,
)