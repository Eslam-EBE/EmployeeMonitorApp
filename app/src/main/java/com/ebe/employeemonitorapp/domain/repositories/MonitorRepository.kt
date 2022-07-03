package com.ebe.employeemonitorapp.domain.repositories

import com.ebe.employeemonitorapp.data.remote.ResultWrapper
import com.ebe.employeemonitorapp.data.remote.requests.EmployeeDetailsRequest
import com.ebe.employeemonitorapp.domain.models.Employee
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import kotlinx.coroutines.flow.Flow

interface MonitorRepository {

    fun getAllEmployees(): Flow<ResultWrapper<List<Employee>>>

    fun getEmployeeDetails(empdetails: EmployeeDetailsRequest): Flow<ResultWrapper<List<EmployeeDetails>>>

    suspend fun updateEmployeePermission(id: String): String
}