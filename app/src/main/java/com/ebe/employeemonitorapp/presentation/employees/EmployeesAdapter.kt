package com.ebe.employeemonitorapp.presentation.employees

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebe.employeemonitorapp.databinding.EmployeeItemBinding
import com.ebe.employeemonitorapp.domain.models.Employee

class EmployeesAdapter :
    ListAdapter<Employee, EmployeesAdapter.EmployeesViewHolder>(EmployeeComparator) {

    private lateinit var binding: EmployeeItemBinding


    inner class EmployeesViewHolder(val binding: EmployeeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun Bind(employee: Employee) {
            binding.empName.text = employee.name

            binding.employeePhone.text = employee.phone

            binding.department.text = employee.department
            binding.root.setOnClickListener {
                employeeClick?.OnClicK(employee)
            }

        }
    }


    object EmployeeComparator : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee) =
            oldItem.phone == newItem.phone

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        binding = EmployeeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EmployeesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {

        val employee = getItem(position)
        holder.Bind(employee)
    }

    var employeeClick: OnEmployeeClick? = null

    interface OnEmployeeClick {
        fun OnClicK(employee: Employee)
    }
}