package com.ebe.employeemonitorapp.presentation.employeedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.DetailItemBinding
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails

class EmployeeDetailsAdapter(var detailsList: List<EmployeeDetails>) :
    RecyclerView.Adapter<EmployeeDetailsAdapter.DetailsViewHolder>() {


    private var binding: DetailItemBinding? = null


    inner class DetailsViewHolder(val binding: DetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(details: EmployeeDetails) {
            binding.name.text = "Name: ${details.name}"
            if (details.ismoked == true) {
                binding.mockingState.text = "Mocked"
                binding.mockingState.setTextColor(R.color.red)
            } else {
                binding.mockingState.text = "Not Mocked"
                binding.mockingState.setTextColor(R.color.white)
            }

            binding.address.text = "Address:"
            binding.attendDate.text = "Date: ${details.time?.substringBefore("T")}"
            binding.attendTime.text = "Time: ${details.time?.substringAfter("T")} "


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
}