package com.teachr.teachr

import java.util.*

data class Entry(val id: Int, var date: String, var duration: Int
                 , var longitude: Int, var latitude: Int, var price: Int, var subject: Subject,
                 var user: String, var type: Int) {

}