package com.fg.mdp.fwgfacilitiesfinder.model.responsSend


import com.google.gson.annotations.SerializedName

data class iTAG(

    @field:SerializedName("version")
    val version: String? = null,

    @field:SerializedName("list")
    val list: List<listItem?>? = null


)