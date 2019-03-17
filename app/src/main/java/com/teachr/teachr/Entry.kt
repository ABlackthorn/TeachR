package com.teachr.teachr

import java.util.*

data class Entry(
        var id: String?,
        var date: String?,
        var duration: Long?,
        var latitude: Double?,
        var longitude: Double?,
        var price: Long?,
        var subject: String?,
        var user: String?,
        var type: Long?
) {

}
