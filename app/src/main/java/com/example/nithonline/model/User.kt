package com.example.nithonline.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val uid:String,
    val userName:String,
    val imgUrl:String
    ):Parcelable{
    constructor(): this("","","")
}