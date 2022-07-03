package com.ebe.employeemonitorapp.data.remote.requests

data class EmployeeDetailsRequest(
    val phone: String,
    val dateFrom: String,
    val dateTo: String
)
