package com.ebe.employeemonitorapp.presentation.employeedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebe.employeemonitorapp.data.remote.ResultWrapper
import com.ebe.employeemonitorapp.data.remote.requests.EmployeeDetailsRequest
import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.ebe.employeemonitorapp.domain.usecases.GetEmployeeDetailsUseCase
import com.ebe.employeemonitorapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(private val useCase: GetEmployeeDetailsUseCase) :
    ViewModel() {


    private var _employeeDetails: MutableLiveData<List<EmployeeDetails>> = MutableLiveData(null)

    val employeeDetails: LiveData<List<EmployeeDetails>>
        get() = _employeeDetails


    private var _loadingState: MutableLiveData<Boolean> = MutableLiveData(true)

    val loadingState: LiveData<Boolean>
        get() = _loadingState


    val errorEvent: SingleLiveEvent<String> = SingleLiveEvent()


    fun getEmployeeDetails(phone: String, firstDate: String, secondDate: String) {
        viewModelScope.launch {

            useCase.invoke(EmployeeDetailsRequest(phone, firstDate, secondDate)).collect {

                when (it) {
                    is ResultWrapper.Success -> {
                        _employeeDetails.value = it.value
                        _loadingState.value = false
                    }

                    is ResultWrapper.Failure -> {
                        errorEvent.value = it.message
                        _loadingState.value = false
                    }

                    is ResultWrapper.Loading -> {
                        _loadingState.value = true
                    }
                }
            }
        }
    }

}