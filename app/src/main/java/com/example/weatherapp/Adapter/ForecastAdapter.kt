package com.example.weatherapp.Adapter

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ForecastViewholderBinding
import com.example.weatherapp.model.ForecastReponseApi
import java.text.SimpleDateFormat

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.Viewholder>() {
    private lateinit var binding: ForecastViewholderBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.Viewholder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ForecastViewholderBinding.inflate(inflater, parent, false)
        return Viewholder()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ForecastAdapter.Viewholder, position: Int) {
        val binding = ForecastViewholderBinding.bind(holder.itemView)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date

        val dateOfWeekName = when(Calendar.DAY_OF_WEEK){
            1-> "Sun"
            2-> "Mon"
            3-> "Tue"
            4-> "Wed"
            5-> "Thur"
            6-> "Fri"
            7-> "Sat"
            else -> "-"
        }
        binding.txtnameday.text = dateOfWeekName
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val amPm = if (hour < 12) "am" else "pm"
        val hour12 = calendar.get(Calendar.HOUR)
        binding.txthour.text = hour12.toString() + amPm
        binding.txttemp.text = differ.currentList[position].main?.temp?.let { Math.round(it) }.toString() + "°"


        val icon = when (differ.currentList[position].weather?.get(0)?.icon.toString()){
            "01d", "0n" -> ""
            "02d", "02n" -> "cloudy_sunny"
            "03d", "03n" -> "cloudy_sunny"
            "04d", "04n" -> "cloudy"
            "09d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11nn" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"
        }
    }
        inner class Viewholder: RecyclerView.ViewHolder(binding.root)
    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<ForecastReponseApi.data>(){
        override fun areItemsTheSame(
            oldItem: ForecastReponseApi.data,
            newItem: ForecastReponseApi.data
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastReponseApi.data,
            newItem: ForecastReponseApi.data
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
}