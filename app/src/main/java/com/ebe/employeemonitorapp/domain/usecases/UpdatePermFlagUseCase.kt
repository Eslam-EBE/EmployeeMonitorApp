package com.ebe.employeemonitorapp.domain.usecases

import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import javax.inject.Inject

class UpdatePermFlagUseCase @Inject constructor(private val repository: MonitorRepository) {


    suspend operator fun invoke(id: String) = repository.updateEmployeePermission(id)


}