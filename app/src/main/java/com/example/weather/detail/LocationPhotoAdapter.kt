package com.example.weather.detail

import android.annotation.SuppressLint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.setTemperatureFormatted
import com.example.weather.setting.TemperatureUnit
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class LocationPhotoAdapter :
    ListAdapter<String, LocationPhotoAdapter.ViewHolder>(LocationPhotoLinkDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photo = itemView.findViewById<ImageView>(R.id.photo)
        fun bind(link: String) {
            Picasso.get().load(link).into(photo)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.location_photo, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

class LocationPhotoLinkDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}