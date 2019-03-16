package com.teachr.teachr

import java.util.*

data class Entry(var id: String?, var date: String, var duration: Int, var geopoint: Geopoint, var price: Int, var subject: Subject,
                 var user: String, var type: Int) {

}
