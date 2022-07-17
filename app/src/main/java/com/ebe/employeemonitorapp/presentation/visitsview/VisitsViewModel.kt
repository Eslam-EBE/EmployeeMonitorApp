package com.ebe.employeemonitorapp.presentation.visitsview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebe.employeemonitorapp.data.remote.ResultWrapper
import com.ebe.employeemonitorapp.data.remote.requests.VisitsByDayRequest
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.ebe.employeemonitorapp.domain.usecases.GetVisitsByDate
import com.ebe.employeemonitorapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VisitsViewModel @Inject constructor(private val getVisitsByDate: GetVisitsByDate) :
    ViewModel() {


    private var _employeesDetails: MutableLiveData<List<EmployeeDetails>> = MutableLiveData(null)

    val employeesDetails: LiveData<List<EmployeeDetails>>
        get() = _employeesDetails


    private var _loadingState: MutableLiveData<Boolean> = MutableLiveData(true)

    val loadingState: LiveData<Boolean>
        get() = _loadingState


    val errorEvent: SingleLiveEvent<String> = SingleLiveEvent()


    fun getVisitsByDate(from: String, to: String) {
        viewModelScope.launch {
            getVisitsByDate.invoke(VisitsByDayRequest(dfrom = from, dTo = to)).collect {

                when (it) {
                    is ResultWrapper.Success -> {
                        _employeesDetails.value = it.value
                        _loadingState.value = false
                    }

                    is ResultWrapper.Loading -> {
                        _loadingState.value = true
                    }

                    is ResultWrapper.Failure -> {
                        errorEvent.value = it.message
                        _loadingState.value = false
                    }
                }
            }
        }
    }
}