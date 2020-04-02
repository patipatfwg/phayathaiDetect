package com.fg.mdp.fwgfacilitiesfinder.model.responsSend

import com.google.gson.annotations.SerializedName


data class itagItem(

    @field:SerializedName("version")
    val version: String = "3003202005001" ,

    @field:SerializedName("list")
    val list: List<listItem?>? = null
)