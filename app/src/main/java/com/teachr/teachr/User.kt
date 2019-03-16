package com.teachr.teachr

data class User(val age: Int, val id: Int, var firstname: String, var lastname: String
                  , var address: String, var email: String, var password: String, var type: Int,
                  var avatar: String? = "") {

}