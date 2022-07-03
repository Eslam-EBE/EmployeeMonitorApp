package com.ebe.employeemonitorapp.domain.usecases

import com.ebe.employeemonitorapp.data.remote.requests.EmployeeDetailsRequest
import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import javax.inject.Inject

class GetEmployeeDetailsUseCase @Inject constructor(private val repository: MonitorRepository) {


    operator fun invoke(empDetails: EmployeeDetailsRequest) =
        repository.getEmployeeDetails(empDetails)
}