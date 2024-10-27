package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


private val retrofit =
    Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create()).build()
val retrofitService = retrofit.create(RetrofitAPI::class.java)


interface RetrofitAPI {
    @GET("weather?q=Haryana&appid=44171420ce0008b50359c3f03bf6f8d9")
    suspend fun getWeatherResults(): WeatherResponse
}