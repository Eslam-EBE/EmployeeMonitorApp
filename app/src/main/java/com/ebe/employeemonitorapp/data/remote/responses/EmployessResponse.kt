package com.ebe.employeemonitorapp.data.remote.responses

import com.ebe.employeemonitorapp.domain.models.Employee

data class EmployeesResponse(

    val employees: List<EmployeeResult>?
) : BaseResponse()

data class EmployeeResult(
    val name: String,
    val phone: String
)


fun List<EmployeeResult>?.toDomain(): List<Employee> {
    return this?.map {
        Employee(name = it.name, phone = it.phone)
    }!!
}
