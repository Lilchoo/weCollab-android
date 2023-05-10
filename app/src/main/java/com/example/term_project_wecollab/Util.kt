package com.example.term_project_wecollab

import java.text.DecimalFormat

class Util {
    companion object {
        private val MONEY_FORMAT = DecimalFormat("#,##0.00")

        fun moneyFormat(number: Number) : String {
            return MONEY_FORMAT.format(number)
        }
    }
}