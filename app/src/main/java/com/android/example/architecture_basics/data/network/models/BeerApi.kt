package com.android.example.architecture_basics.data.network.models

import com.google.gson.annotations.SerializedName

data class BeerApi(

    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("tagline") var tagline: String,
    @SerializedName("first_brewed") var firstBrewed: String,
    @SerializedName("description") var description: String,
    @SerializedName("image_url") var imageUrl: String,
    @SerializedName("abv") var abv: Double,
    @SerializedName("ibu") var ibu: Double,
)
