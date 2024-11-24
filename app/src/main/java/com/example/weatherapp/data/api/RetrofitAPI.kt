package com.example.weatherapp.data.api

import com.example.weatherapp.utils.CONSTANTS
import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val retrofit =
    Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create()).build()
val retrofitService = retrofit.create(RetrofitAPI::class.java)


interface RetrofitAPI {
    @GET("weather")
    suspend fun getWeatherResults(
        @Query("q") query: String,
        @Query("appid") apikey: String = CONSTANTS.API_KEY
    ): WeatherResponse
}