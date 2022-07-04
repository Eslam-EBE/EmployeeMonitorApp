package com.ebe.employeemonitorapp.data.remote

import com.ebe.employeemonitorapp.data.remote.requests.EmployeeDetailsRequest
import com.ebe.employeemonitorapp.data.remote.requests.PermissionRequest
import com.ebe.employeemonitorapp.data.remote.responses.BaseResponse
import com.ebe.employeemonitorapp.data.remote.responses.EmployeeDetailsResponse
import com.ebe.employeemonitorapp.data.remote.responses.EmployeesResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MonitorService {


    @GET("getAll")
    suspend fun getAllEmployees(): ApiResponse<EmployeesResponse>

    @POST("getAttendancebyphone")
    suspend fun getEmployeeDetails(@Body detailsRequest: EmployeeDetailsRequest): ApiResponse<EmployeeDetailsResponse>

    @POST("ResetUpdateflag")
    suspend fun updatePermission(@Body emp_id: PermissionRequest): ApiResponse<BaseResponse>
}