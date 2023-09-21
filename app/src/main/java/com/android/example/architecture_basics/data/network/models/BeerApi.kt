package com.android.example.architecture_basics.data.network.models

import com.android.example.architecture_basics.data.database.entity.BeerEntity
import com.google.gson.annotations.SerializedName

data class BeerApi(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("tagline") var tagline: String? = null,
    @SerializedName("first_brewed") var firstBrewed: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("abv") var abv: Double? = null,
    @SerializedName("ibu") var ibu: Double? = null,
)

fun BeerEntity.toApi() = BeerApi(
    id = id,
    name = name,
    tagline = tagline,
    firstBrewed = firstBrewed,
    description = description,
    imageUrl = imageUrl,
    abv = abv,
    ibu = ibu
)