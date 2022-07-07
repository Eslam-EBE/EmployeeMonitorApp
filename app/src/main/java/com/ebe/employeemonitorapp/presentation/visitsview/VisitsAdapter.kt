package com.ebe.employeemonitorapp.presentation.visitsview

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.DetailItemBinding
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.ebe.employeemonitorapp.utils.getAddress
import com.ebe.employeemonitorapp.utils.getTime
import com.ivoberger.statikgmapsapi.core.StatikGMapsUrl
import com.ivoberger.statikgmapsapi.core.StatikMapsLocation

class VisitsAdapter(var detailsList: List<EmployeeDetails>) :
    RecyclerView.Adapter<VisitsAdapter.VisitsViewHolder>() {


    var context: Context? = null
    private var binding: DetailItemBinding? = null
    var geocoder: Geocoder? = null


    inner class VisitsViewHolder(val binding: DetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(details: EmployeeDetails) {
            binding.name.text = "Name: ${details.name}"
            if (details.ismoked == true) {
                binding.mockingState.text = "Mocked"
                binding.mockingState.setTextColor(context?.resources?.getColor(R.color.red, null)!!)
            } else {
                binding.mockingState.setTextColor(
                    context?.resources?.getColor(
                        R.color.white,
                        null
                    )!!
                )
                binding.mockingState.text = "Not Mocked"

            }

            val address =
                if (details.lat != null && details.long != null && geocoder != null) getAddress(
                    details.lat.toDouble(),
                    details.long.toDouble(), geocoder!!
                ) else ""
            binding.address.text = "Address: $address"
            binding.attendDate.text = "Date: ${details.time?.substringBefore("T")}"
            binding.attendTime.text = "Time: ${getTime(details.time?.substringAfter("T")!!)} "

            binding.mapImg.setOnClickListener {
                getMapLocation?.getMap(
                    details.lat?.toDouble()!!,
                    details.long?.toDouble()!!,
                    address
                )
            }

            Glide.with(binding.mapImg)
                .load(
                    getMapImgURL(
                        details.lat?.toDouble()!!,
                        details.long?.toDouble()!!,
                        address
                    )
                )
                .placeholder(R.drawable.egyptmap)
                .into(binding.mapImg)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitsViewHolder {
        binding = DetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VisitsViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: VisitsViewHolder, position: Int) {

        val item = detailsList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    private fun getMapImgURL(lat: Double, long: Double, address: String): String {
        val staticMap = StatikGMapsUrl("AIzaSyA2MSn1f0oSHcG1LonhFkOg0eYImEl8xgE") {
            size = 500 to 250
            center = StatikMapsLocation(lat, long)
            markers = mutableListOf(
                StatikMapsLocation(lat, long),
                StatikMapsLocation(address = address),
                StatikMapsLocation(lat, long)
            )
            zoom = 10
            scale = 2
        }
// get the url, this is where all specification checks are performed

        return staticMap.toString()
    }

    var getMapLocation: VisitsGetMapLocation? = null

    interface VisitsGetMapLocation {
        fun getMap(lat: Double, long: Double, address: String)
    }
}