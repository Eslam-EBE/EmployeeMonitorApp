package com.ebe.employeemonitorapp.data.repositories

import com.ebe.employeemonitorapp.data.remote.MonitorService
import com.ebe.employeemonitorapp.data.remote.ResultWrapper
import com.ebe.employeemonitorapp.data.remote.requests.EmployeeDetailsRequest
import com.ebe.employeemonitorapp.domain.models.Employee
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MonitorRepositoryImpl @Inject constructor(private val service: MonitorService) :
    MonitorRepository {
    override fun getAllEmployees(): Flow<ResultWrapper<List<Employee>>> {
        TODO("Not yet implemented")
    }

    override fun getEmployeeDetails(empdetails: EmployeeDetailsRequest): Flow<ResultWrapper<List<EmployeeDetails>>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateEmployeePermission(id: String): String {
        TODO("Not yet implemented")
    }
}