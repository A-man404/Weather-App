package com.example.weatherapp.screens

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.api.retrofitService
import com.example.weatherapp.data.model.WeatherCategory
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private val _dataState = mutableStateOf(DataState())
    var dataStatePublic: State<DataState> = _dataState
    private var place = "haryana"

    fun updatePlace(newPlace: String) {
        place = newPlace
        fetchCategories()
    }

    init {
        fetchCategories()
    }
    fun fetchCategories() {
        _dataState.value = _dataState.value.copy(loading = true)
        viewModelScope.launch {
            try {
                val response = retrofitService.getWeatherResults(place)
                _dataState.value = _dataState.value.copy(
                    loading = false,
                    error = null,
                    list = listOf(
                        WeatherCategory(
                            weather = response.weather,
                            main = response.main,
                            visibility = response.visibility,
                            wind = response.wind,
                            name = response.name,
                        )
                    )
                )

            } catch (e: Error) {
                _dataState.value = _dataState.value.copy(
                    loading = false,
                    error = "There is an error${e.message}",
                    list = emptyList()
                )
                Log.d("ERROR", "fetchCategories: ${e.message}")
            }

        }
    }


    data class DataState(
        val loading: Boolean = true,
        val error: String? = null,
        val list: List<WeatherCategory> = emptyList()
    )
}


