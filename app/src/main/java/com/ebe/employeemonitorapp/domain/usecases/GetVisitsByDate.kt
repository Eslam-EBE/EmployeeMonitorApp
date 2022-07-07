package com.ebe.employeemonitorapp.domain.usecases

import com.ebe.employeemonitorapp.data.remote.requests.VisitsByDayRequest
import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import javax.inject.Inject

class GetVisitsByDate @Inject constructor(private val repository: MonitorRepository) {

    operator fun invoke(request: VisitsByDayRequest) = repository.getVisitsByDate(request)
}