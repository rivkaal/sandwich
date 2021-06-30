package com.example.sandwich

import java.io.Serializable
import java.util.*

data class Order(
    var customer_name: String = "",
    var pickles: Int = 0,
    var hummus: Boolean = false,
    var tahini: Boolean = false,
    var comment: String = ""
) : Serializable {
    var id = ""
    var status = "waiting"

    init {
        id = UUID.randomUUID().toString()
    }

}