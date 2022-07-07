package com.ebe.employeemonitorapp.presentation.employees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebe.employeemonitorapp.data.remote.ResultWrapper
import com.ebe.employeemonitorapp.domain.models.Employee
import com.ebe.employeemonitorapp.domain.usecases.GetEmployeesUseCase
import com.ebe.employeemonitorapp.domain.usecases.UpdatePermFlagUseCase
import com.ebe.employeemonitorapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val permFlagUseCase: UpdatePermFlagUseCase

) :
    ViewModel() {


    private var _employees: MutableLiveData<List<Employee>?> = MutableLiveData()

    val employees: LiveData<List<Employee>?>
        get() = _employees


    private var _loadingState: MutableLiveData<Boolean> = MutableLiveData(true)

    val loadingState: LiveData<Boolean>
        get() = _loadingState


    private val errorEventChannel = Channel<String>(Channel.BUFFERED)
    val errorEventFlow = errorEventChannel.receiveAsFlow()

    val permState: SingleLiveEvent<String> = SingleLiveEvent()


    fun getAllEmployees() {
        viewModelScope.launch {

            getEmployeesUseCase.invoke().collect {

                when (it) {
                    is ResultWrapper.Success -> {
                        _employees.value = it.value
                        _loadingState.value = false
                    }

                    is ResultWrapper.Failure -> {

                        errorEventChannel.send(it.message!!)
                        _loadingState.value = false
                    }

                    is ResultWrapper.Loading -> {

                        _loadingState.value = true
                    }
                }

            }
        }


    }

    fun givePermission(employee: Employee) {

        viewModelScope.launch {
            val permissionState = permFlagUseCase.invoke(employee.phone)

            if (permissionState == 1) {

                permState.value = ("Permission Granted for ${employee.name}")

            } else {
                permState.value = ("Permission Request Failed")


            }
        }


    }

}