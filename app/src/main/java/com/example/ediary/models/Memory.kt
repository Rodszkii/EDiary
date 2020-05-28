package com.example.ediary.models

import android.net.Uri
import android.widget.ImageView
import java.util.*

data class Memory (val IDdb:String,val id: Int, val title: String, val description: String, val date :String, val imageURL:String) {//uri
    constructor():this("",0,"","","","")

}