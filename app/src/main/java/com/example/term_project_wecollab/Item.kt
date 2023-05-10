package com.example.term_project_wecollab

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Item(
    val category: String = "",
    val desc: String = "",
    val image: String = "",
    val itemId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val sizes: ArrayList<String> = arrayListOf(),
    var amount: Int = 0,
    private var _sizeSelected: String = ""
) : Parcelable {

    var sizeSelected: String
        get() = _sizeSelected
        set(value) {_sizeSelected = value}


}

