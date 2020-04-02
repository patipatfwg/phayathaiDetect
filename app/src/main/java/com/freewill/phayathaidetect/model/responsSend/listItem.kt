package com.fg.mdp.fwgfacilitiesfinder.model.responsSend

import com.google.gson.annotations.SerializedName


data class listItem(

    @field:SerializedName("distance")
    val distance: Short ? = null,

    @field:SerializedName("mac_address")
    val macAddress: String? = null,

    @field:SerializedName("name")
    val name: String? = "ABC"


)