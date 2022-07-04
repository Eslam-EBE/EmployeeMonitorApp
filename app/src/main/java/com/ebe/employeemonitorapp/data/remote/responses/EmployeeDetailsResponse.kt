package com.ebe.employeemonitorapp.data.remote.responses

import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.google.gson.annotations.SerializedName

data class EmployeeDetailsResponse(

    @field:SerializedName("result")
    val result: List<ResultItem?>? = null
) : BaseResponse()

data class ResultItem(

    @field:SerializedName("empId")
    val empId: String? = null,

    @field:SerializedName("ismoked")
    val ismoked: Boolean? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("time")
    val time: String? = null,

    @field:SerializedName("lang")
    val lang: String? = null,

    @field:SerializedName("lat")
    val lat: String? = null
)


fun List<ResultItem?>?.toDomain(): List<EmployeeDetails> {
    return this?.map {
        EmployeeDetails(
            phone = it?.empId,
            ismoked = it?.ismoked,
            name = it?.name,
            time = it?.time,
            long = it?.lang,
            lat = it?.lat
        )
    }!!
}
