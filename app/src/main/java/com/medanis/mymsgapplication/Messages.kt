package com.medanis.mymsgapplication

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Messages(
    var name : String? = "",
    var mUID : String? = "",
    var time : String? = "isSameDay().timeNow()",
    var message : String? = ""
)