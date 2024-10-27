package com.example.weatherapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import java.time.LocalDate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.format.DateTimeFormatter


@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.dataStatePublic


    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(1f)
    ) {
        when {
            state.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color(0xffffe142))
                )
                Log.d("FIXXXXX", "MainScreen: it is loading")
            }

            state.error != null -> {
                Text(
                    "${state.error}", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp
                )
            }

            else -> {
                Items(state.list)
            }
        }
//        FloatingActionButton(onClick = { viewModel.fetchCategories() }) {
//            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
//        }
        IconButton(onClick = { viewModel.fetchCategories() }, modifier = Modifier.align(Alignment.TopEnd).padding(top = 24.dp)) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
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

//@Preview(showSystemUi = true)
@SuppressLint("NewApi", "DefaultLocale")
@Composable
fun EachItem(category: WeatherCategory) {


    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM EEEE"))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xffffe142))
            .padding(38.dp)
    ) {
//        Spacer(Modifier.height(16.dp))
        Text(
            category.name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
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
                fontSize = 20.sp, color = Color(0xffffe142)
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
                //fontFamily = iosevka
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
                DataItem(R.drawable.wind, category.wind.speed.toInt().toString() + "km/h", "Wind")
                DataItem(R.drawable.humidity, category.main.humidity.toString() + "%", "Humidity")
                DataItem(R.drawable.visibility, visibilityInKm.toString() + "Km", "Visibility")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("More Information", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

            val tempMaxInCal = category.main.tempMax - 273.15
            val tempMinInCal = category.main.tempMin - 273.15
            MoreInfoData(category.main.pressure.toString(), "Pressure", R.drawable.pressure)
            MoreInfoData(
                tempMaxInCal.toInt().toString() + "°C", "Max Temp", R.drawable.outline_wb_sunny_24
            )
            MoreInfoData(
                tempMinInCal.toInt().toString() + "°C", "Min Temp", R.drawable.outline_wb_cloudy_24
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
fun DataItem(icon: Int, data: String, name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(data, color = Color(0xffffe142), fontSize = 20.sp)
        Spacer(Modifier.height(5.dp))
        Text(name, color = Color(0xffffe142), fontSize = 12.sp)
    }

}












