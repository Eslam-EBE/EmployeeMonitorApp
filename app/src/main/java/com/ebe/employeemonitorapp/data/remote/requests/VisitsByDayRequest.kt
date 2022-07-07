package com.ebe.employeemonitorapp.data.remote.requests

data class VisitsByDayRequest(
	val ismoked: String? = null,
	val phone: String? = null,
	val name: String? = null,
	val dTo: String? = null,
	val dfrom: String? = null
)

