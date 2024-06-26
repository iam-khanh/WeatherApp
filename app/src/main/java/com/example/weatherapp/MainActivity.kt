package com.example.weatherapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.ViewModel.WeatherViewModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.CurrentResponseApi
import com.github.matteobattilana.weather.PrecipType
import eightbitlab.com.blurview.RenderScriptBlur
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Kiểm tra phiên bản Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Sử dụng WindowInsetsController để ẩn thanh trạng thái và thanh điều hướng
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Sử dụng phương pháp cũ hơn cho các phiên bản Android thấp hơn
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        binding.apply {
            var lat = 10.8231
            var lon = 106.6297
            var name = "Ho Chi Minh City"

            // Current Temp
            txtcity.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if(response.isSuccessful)
                    {
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE

                        data?.let {
                            txtStatus.text = it.weather?.get(0)?.main ?: "-"
                            txtwind.text = it.wind?.speed?.let { Math.round(it).toString() } + "km"
                            txthummidity.text = it?.main?.humidity.toString() + "%"
                            txtCurrentTemp.text=  it.main?.temp?.let { Math.round(it).toString() } + "°"
                            txtmaxtemp.text=  it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                            txtmintemp.text=  it.main?.tempMin?.let { Math.round(it).toString() } + "°"

                            val drawable =
                                if(isNightNow())
                                {
                                    R.drawable.night_bg
                                }else
                                {
                                    setDynamicallyWallpaper(it.weather?.get(0)?.icon?: "-")
                                }
                            bgimage.setImageResource(drawable)
                            setEffectRainNow(it.weather?.get(0)?.icon?: "-")
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }

            })

            // Setting BlueView
            var radius = 10f
            val decorView = window.decorView
            val rootView = (decorView.findViewById(android.R.id.content) as ViewGroup)
            val windowbackground = decorView.background

            rootView?.let {
                blueview.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowbackground)
                    .setBlurRadius(radius)
                blueview.outlineProvider = ViewOutlineProvider.BACKGROUND
                blueview.clipToOutline = true
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun isNightNow(): Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynamicallyWallpaper(icon: String): Int{
            return when(icon.dropLast(1)){
                "01" -> {
                    initWeatherView(PrecipType.CLEAR)
                    R.drawable.snow_bg
                }
                "02","03", "04" -> {
                    initWeatherView(PrecipType.CLEAR)
                    R.drawable.cloudy_bg
                }
                "09","10","11" -> {
                    initWeatherView(PrecipType.RAIN)
                    R.drawable.rainy_bg
                }
                "13" -> {
                    initWeatherView(PrecipType.SNOW)
                    R.drawable.snow_bg
                }
                "50" -> {
                    initWeatherView(PrecipType.SNOW)
                    R.drawable.haze_bg
                }
                else ->0
            }
    }

    private fun setEffectRainNow(icon: String) {
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)

            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)

            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)

            }

            "13" -> {
                initWeatherView(PrecipType.SNOW)

            }

            "50" -> {
                initWeatherView(PrecipType.SNOW)

            }
        }
    }

    private fun initWeatherView(type: PrecipType){
        binding.WeatherView.apply {
            setWeatherData(type)
            angle = 20
            emissionRate = 100.0f
        }
    }
}