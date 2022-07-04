package com.ebe.employeemonitorapp.data.remote.responses

import com.google.gson.annotations.SerializedName

open class BaseResponse(

    @field:SerializedName("response")
    val response: Response? = null,

    )

data class Response(

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("code")
    val code: Int? = null,

)
