package com.ebe.employeemonitorapp.domain.usecases

import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(private val repository: MonitorRepository) {


    operator fun invoke() = repository.getAllEmployees()
}