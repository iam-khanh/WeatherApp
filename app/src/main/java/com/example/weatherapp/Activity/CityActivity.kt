package com.example.weatherapp.Activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.Adapter.CityAdapter
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.CityViewModel
import com.example.weatherapp.databinding.ActivityCityBinding
import com.example.weatherapp.model.CityResponseApi
import com.example.weatherapp.model.CityResponseApiItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCityBinding
    private val  cityAdapter by lazy { CityAdapter() }
    private val cityViewModel: CityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            edtcity.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    progressBar2.visibility = View.VISIBLE
                    cityViewModel.Loadcity(s.toString(), 10).enqueue(object  : Callback<CityResponseApi>{
                        override fun onResponse(
                            call: Call<CityResponseApi>,
                            response: Response<CityResponseApi>
                        ) {
                            if(response.isSuccessful){
                                val data = response.body()
                                progressBar2.visibility = View.GONE
                                cityAdapter.differ.submitList(data)
                                rvcity.apply {
                                    layoutManager = LinearLayoutManager(this@CityActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false)
                                    adapter = cityAdapter
                                }
                            }
                        }

                        override fun onFailure(call: Call<CityResponseApi>, t: Throwable) {

                        }

                    })
                }
            })
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}