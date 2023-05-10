package com.example.term_project_wecollab

import kotlin.random.Random

class OrderNumberGenerator {
    companion object {

        const val SEQUENCE = "ABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789"

        @JvmStatic
        fun generate() : String {
            var orderNumber = ""
            for (i in 1..6) {
                orderNumber = orderNumber.plus(SEQUENCE[Random.nextInt(0, SEQUENCE.length)])
            }
            return orderNumber
        }
    }
}