package com.fg.mdp.fwgfacilitiesfinder.model.responsSend


import com.google.gson.annotations.SerializedName

data class Information(

	@field:SerializedName("device_id")
	val device_id: String? = "50321a567585340f",

//	@field:SerializedName("deviceId")
//	val deviceId: String.Companion = null,


	@field:SerializedName("datetime")
	val datetime: String? = null

)