package com.ebe.employeemonitorapp.presentation.employees

import androidx.lifecycle.ViewModel
import com.ebe.employeemonitorapp.domain.usecases.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(private val getEmployeesUseCase: GetEmployeesUseCase) :
    ViewModel() {


}