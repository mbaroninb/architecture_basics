package com.android.example.architecture_basics.domain.model

import com.android.example.architecture_basics.data.database.entity.BeerEntity
import com.android.example.architecture_basics.data.network.models.BeerApi

/*
* Creo esta clase para convertir tanto los BeerApi como los BeerEntity antes de mostrarlos en la
* UI
* */
data class BeerDomain(
    var id: Int,
    var name: String,
    var tagline: String,
    var firstBrewed: String,
    var description: String,
    var imageUrl: String,
    var abv: Double,
    var ibu: Double,
)

fun BeerApi.toDomain() = BeerDomain(
    id = id!!,
    name = name!!,
    tagline = tagline!!,
    firstBrewed = firstBrewed!!,
    description = description!!,
    imageUrl = imageUrl!!,
    abv = abv!!,
    ibu = ibu!!
)

fun BeerEntity.toDomain() = BeerDomain(
    id = id,
    name = name,
    tagline = tagline,
    firstBrewed = firstBrewed,
    description = description,
    imageUrl = imageUrl,
    abv = abv,
    ibu = ibu
)