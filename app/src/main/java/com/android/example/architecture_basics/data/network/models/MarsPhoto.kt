package com.android.example.architecture_basics.data.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarsPhoto(

    val id: String,
    @SerializedName("img_src")
    val imgSrcUrl: String

):Parcelable
