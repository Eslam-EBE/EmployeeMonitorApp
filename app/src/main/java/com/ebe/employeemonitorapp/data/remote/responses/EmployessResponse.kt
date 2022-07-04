package com.ebe.employeemonitorapp.data.remote.responses

import com.ebe.employeemonitorapp.domain.models.Employee
import com.google.gson.annotations.SerializedName

data class EmployeesResponse(

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("code")
    val code: Int? = null,
    @field:SerializedName("result")
    val employees: List<EmployeeResult>?
)

data class EmployeeResult(
    val Name: String,
    val Emp_Id: String,
    val Department: String
)


fun List<EmployeeResult>?.toDomain(): List<Employee> {
    return this?.map {
        Employee(name = it.Name, phone = it.Emp_Id, department = it.Department)
    }!!
}
