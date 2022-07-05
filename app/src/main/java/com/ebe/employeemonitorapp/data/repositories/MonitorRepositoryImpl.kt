package com.ebe.employeemonitorapp.data.repositories

import com.ebe.employeemonitorapp.data.remote.MonitorService
import com.ebe.employeemonitorapp.data.remote.ResultWrapper
import com.ebe.employeemonitorapp.data.remote.requests.EmployeeDetailsRequest
import com.ebe.employeemonitorapp.data.remote.requests.PermissionRequest
import com.ebe.employeemonitorapp.data.remote.responses.toDomain
import com.ebe.employeemonitorapp.domain.models.Employee
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import com.skydoves.sandwich.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MonitorRepositoryImpl @Inject constructor(private val service: MonitorService) :
    MonitorRepository {
    override fun getAllEmployees(): Flow<ResultWrapper<List<Employee>>> {

        return flow {
            emit(ResultWrapper.Loading())
            val result = service.getAllEmployees()

            result.suspendOnSuccess {
                emit(ResultWrapper.Success(this.data.employees.toDomain()))
            }

            result.suspendOnFailure {
                emit(ResultWrapper.Failure(this, null))
            }
        }.catch {

            emit(ResultWrapper.Failure("Error Loading Data Open Your Internet Connection", null))
        }.flowOn(Dispatchers.IO)

    }

    override fun getEmployeeDetails(empdetails: EmployeeDetailsRequest): Flow<ResultWrapper<List<EmployeeDetails>>> {
        var errorMsg = ""
        return flow {
            emit(ResultWrapper.Loading())

            val result = service.getEmployeeDetails(empdetails)



            result.suspendOnSuccess {
                if (this.data.response?.msg?.isNotEmpty()!!) {
                    errorMsg = this.data.response?.msg!!
                    emit(
                        ResultWrapper.Failure(
                            if (errorMsg.isNotEmpty()) errorMsg else "Error Loading Employee Details",
                            null
                        )
                    )
                } else
                    emit(ResultWrapper.Success(this.data.result.toDomain()))
            }
            result.suspendOnError {
                emit(ResultWrapper.Failure(this.message(), this.statusCode.code))
            }
        }.catch {
            emit(
                ResultWrapper.Failure(
                    if (errorMsg.isNotEmpty()) errorMsg else "Error Loading Employee Details",
                    null
                )
            )
        }
    }

    override suspend fun updateEmployeePermission(id: String): Int {
        var success = 1
        try {
            val result = service.updatePermission(PermissionRequest(id))

            result.onSuccess {
                success = this.data.response?.code!!
            }

            result.onError {
                success = this.statusCode.code
            }

            return success


        } catch (e: Exception) {
            return 0
        }
    }
}