package com.app.ynvest_tube.model.internal

class Duration(text: String) {

    val days: Int = text.substring(text.indexOf('P') + 1, text.indexOf('D')).toInt()
    val hours: Int = text.substring(text.indexOf('T') + 1, text.indexOf('H')).toInt()

    override fun toString(): String {
        return if (days > 0) {
            "${days}d ${hours}h"
        } else {
            "${hours}h"
        }
    }
}