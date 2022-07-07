package com.ebe.employeemonitorapp.data.remote.responses

import com.ebe.employeemonitorapp.domain.models.EmployeeDetails
import com.google.gson.annotations.SerializedName

data class VisitsByDayResponse(

	@field:SerializedName("result")
	val result: List<VisitResultItem?>? = null,

	@field:SerializedName("response")
	val response: VisitsResponse? = null
)

data class VisitsResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("change_perm")
	val changePerm: Boolean? = null
)

data class VisitResultItem(

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


fun List<VisitResultItem?>?.toDomain(): List<EmployeeDetails> {
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
