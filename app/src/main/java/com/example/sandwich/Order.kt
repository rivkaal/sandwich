package com.example.sandwich

import java.io.Serializable
import java.util.*

class Order : Serializable {
    var id = ""
    var customer_name = ""
    var pickles = 0
    var hummus = false
    var tahini = false
    var comment = ""
    var status = "waiting"

    constructor( customer_name:String = "", pickles:Int = 0,
    hummus:Boolean = false, tahini:Boolean = false, comment:String = "")
    {
        id = UUID.randomUUID().toString()
        this.customer_name = customer_name
        this.pickles = pickles
        this.hummus = hummus
        this.tahini = tahini
        this.comment = comment
    }

}