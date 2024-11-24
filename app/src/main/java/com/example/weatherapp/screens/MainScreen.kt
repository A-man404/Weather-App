package com.example.weatherapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherCategory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()

    val state by viewModel.dataStatePublic
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor = when {
        state.list.isNotEmpty() -> {
            when (state.list[0].weather[0].main) {
                "Thunderstorm" -> Color(0xff6C3483)
                "Drizzle" -> Color(0xffA3C1AD)
                "Rain" -> Color(0xff3A6EA5)
                "Snow" -> Color(0xffE8F8F5)
                "Atmosphere" -> Color(0xff7D7D7D)
                "Clear" -> Color(0xffF4D03F)
                "Clouds" -> Color(0xffD6DBDF)
                else -> Color(0xffCFCFCF)
            }
        }

        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 16.dp)
    ) {
        when {
            state.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.error != null -> {
                Text(
                    "${state.error}", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp
                )
            }

            else -> {
                PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        viewModel.fetchCategories()
                        isRefreshing = false
                    }
                }) {
                    Items(state.list)
                }
            }
        }
    }
}

@Composable
fun Items(weatherItem: List<WeatherCategory>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(weatherItem) { item ->
            EachItem(item)
        }
    }
}

@SuppressLint("NewApi", "DefaultLocale")
@Composable
fun EachItem(category: WeatherCategory) {
    val viewModel: MainViewModel = viewModel()
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM EEEE"))
    var currentBg by remember { mutableStateOf(Color.White) }
    val isDialogBoxOpen = remember { mutableStateOf(false) }
    val placeData = remember { mutableStateOf("") }
    currentBg = when (category.weather[0].main) {
        "Thunderstorm" -> Color(0xff6C3483)
        "Drizzle" -> Color(0xffA3C1AD)
        "Rain" -> Color(0xff3A6EA5)
        "Snow" -> Color(0xffE8F8F5)
        "Atmosphere" -> Color(0xff7D7D7D)
        "Clear" -> Color(0xffF4D03F)
        "Clouds" -> Color(0xffD6DBDF)
        else -> Color(0xffCFCFCF)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(38.dp)
        ) {
            Text(
                " ✎ " + category.name,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        isDialogBoxOpen.value = true
                    },
                style = TextStyle(
                    fontWeight = FontWeight.Bold, fontSize = 28.sp
                )
            )
            Spacer(Modifier.height(42.dp))
            Text(
                currentDate.toString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.Black, shape = RoundedCornerShape(22.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                style = TextStyle(
                    fontSize = 20.sp, color = currentBg
                ),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                category.weather[0].main,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontSize = 25.sp
                )
            )
            val tempInCal = category.main.temp - 273.5
            Text(
                tempInCal.toInt().toString() + "°",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontSize = 190.sp,
                )
            )
            Spacer(Modifier.height(10.dp))
            Text("Little Summary", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            val feelsLikeInCal = category.main.feelsLike - 273.15

            Text(
                "Now it feels like ${
                    String.format(
                        "%.1f", feelsLikeInCal
                    )
                }°, actually ${
                    String.format(
                        "%.1f", tempInCal
                    )
                }°, and if you want more information then step outside and look for yourself",
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, shape = RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val visibilityInKm = category.visibility / 1000
                    DataItem(
                        R.drawable.wind,
                        category.wind.speed.toInt().toString() + "km/h",
                        "Wind",
                        currentBg
                    )
                    DataItem(
                        R.drawable.humidity,
                        category.main.humidity.toString() + "%",
                        "Humidity",
                        currentBg
                    )
                    DataItem(
                        R.drawable.visibility,
                        visibilityInKm.toString() + "Km",
                        "Visibility",
                        currentBg
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("More Information", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {

                val tempMaxInCal = category.main.tempMax - 273.15
                val tempMinInCal = category.main.tempMin - 273.15
                MoreInfoData(category.main.pressure.toString(), "Pressure", R.drawable.pressure)
                MoreInfoData(
                    tempMaxInCal.toInt().toString() + "°C",
                    "Max Temp",
                    R.drawable.outline_wb_sunny_24
                )
                MoreInfoData(
                    tempMinInCal.toInt().toString() + "°C",
                    "Min Temp",
                    R.drawable.outline_wb_cloudy_24
                )
            }
        }
        if (isDialogBoxOpen.value) {
            AlertDialog(
                onDismissRequest = { isDialogBoxOpen.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        isDialogBoxOpen.value = false
                        viewModel.updatePlace(placeData.value.trim())
                    }) { Text("Submit", color = Color.Black) }

                },
                title = { Text("Enter your city", color = Color.Black) },
                text = {
                    OutlinedTextField(
                        onValueChange = { placeData.value = it },
                        value = placeData.value,
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black)
                    )
                },
                containerColor = currentBg
            )

        }
    }
}

@Composable
fun MoreInfoData(value: String, name: String, icon: Int) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .width(80.dp)
            .background(Color(0x00000000), shape = RoundedCornerShape(16.dp))
            .border(width = 2.dp, shape = RoundedCornerShape(16.dp), color = Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Image(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(value)
        }
    }
}

@Composable
fun DataItem(icon: Int, data: String, name: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(Modifier.height(10.dp))
        Text(data, color = color, fontSize = 20.sp)
        Spacer(Modifier.height(5.dp))
        Text(name, color = color, fontSize = 12.sp)
    }

}












