package com.fg.mdp.fwgfacilitiesfinder.model.responsSend


import com.google.gson.annotations.SerializedName


data class ResponsDetecPhaythai(


	@field:SerializedName("itag")
	val itag: List<itagItem?>? = null,

	@field:SerializedName("androidbox")
	val androidbox: Information? = null
)
