package com.example.weatherapp.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.Activity.MainActivity
import com.example.weatherapp.databinding.CityViewholderBinding
import com.example.weatherapp.databinding.ForecastViewholderBinding
import com.example.weatherapp.model.CityResponseApi
import com.example.weatherapp.model.CityResponseApiItem
import com.example.weatherapp.model.ForecastReponseApi
import java.text.SimpleDateFormat

class CityAdapter: RecyclerView.Adapter<CityAdapter.Viewholder>() {
    private lateinit var binding: CityViewholderBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.Viewholder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CityViewholderBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
        }
        return Viewholder()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CityAdapter.Viewholder, position: Int) {
        val binding = CityViewholderBinding.bind(holder.itemView)

    }
        inner class Viewholder: RecyclerView.ViewHolder(binding.root)
    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<CityResponseApi>(){
        override fun areItemsTheSame(
            oldItem: CityResponseApi,
            newItem: CityResponseApi
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem:  CityResponseApi,
            newItem:  CityResponseApi
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
}