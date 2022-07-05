package com.ebe.employeemonitorapp.presentation.employeedetails

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.DetailItemBinding
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat

class EmployeeDetailsAdapter(var detailsList: List<EmployeeDetails>) :
    RecyclerView.Adapter<EmployeeDetailsAdapter.DetailsViewHolder>() {


    var scope: CoroutineScope? = null
    private var binding: DetailItemBinding? = null
    var geocoder: Geocoder? = null
    var context: Context? = null


    inner class DetailsViewHolder(val binding: DetailItemBinding) :
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

            val address = if (details.lat != null && details.long != null) getAddress(
                details.lat.toDouble(),
                details.long.toDouble()
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


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        binding = DetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DetailsViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val details = detailsList[position]

        holder.bind(details)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }


    fun submitList(detailsList: List<EmployeeDetails>) {
        this.detailsList = detailsList
        notifyDataSetChanged()
    }


    private fun getAddress(lat: Double, long: Double): String {

        try {


            geocoder?.getFromLocation(lat, long, 1)?.first()
                .run {
                    val sb = StringBuilder()
                    for (i in 0 until this?.maxAddressLineIndex!!) {
                        sb.append(this.getAddressLine(i))
                    }
                    sb.append(this.locality)
                    sb.append(this.thoroughfare)
                    //  sb.append(this.countryName)
                    return sb.toString()


                }
        } catch (e: Exception) {

            Log.d("adressList", "onLocationSelected: $e.localizedMessage")
            e.localizedMessage!!
            return ""
        }


    }


    fun getTime(time: String): String {
        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt = sdf.parse(time);

        return sdfs.format(dt!!)
    }


    var getMapLocation: GetMapLocation? = null

    interface GetMapLocation {
        fun getMap(lat: Double, long: Double, address: String)
    }


}


