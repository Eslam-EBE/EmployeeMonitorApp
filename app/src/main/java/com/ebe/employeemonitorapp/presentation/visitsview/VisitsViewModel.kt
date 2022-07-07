package com.ebe.employeemonitorapp.presentation.visitsview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebe.employeemonitorapp.data.remote.requests.VisitsByDayRequest
import com.ebe.employeemonitorapp.domain.usecases.GetVisitsByDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VisitsViewModel @Inject constructor(private val getVisitsByDate: GetVisitsByDate) :
    ViewModel() {


    fun getVisitsByDate(from: String, to: String) {
        viewModelScope.launch {
            getVisitsByDate.invoke(VisitsByDayRequest(dfrom = from, dTo = to)).collect {

            }
        }
    }
}