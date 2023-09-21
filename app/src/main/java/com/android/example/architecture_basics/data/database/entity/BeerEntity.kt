package com.android.example.architecture_basics.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.example.architecture_basics.data.network.models.BeerApi

@Entity(tableName = "beers")
data class BeerEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "tagline")
    var tagline: String,
    @ColumnInfo(name = "firstBrewed")
    var firstBrewed: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String,
    @ColumnInfo(name = "abv")
    var abv: Double,
    @ColumnInfo(name = "ibu")
    var ibu: Double,
)

fun BeerApi.toEntity() = BeerEntity(
    id = id!!,
    name = name!!,
    tagline = tagline!!,
    firstBrewed = firstBrewed!!,
    description = description!!,
    imageUrl = imageUrl!!,
    abv = abv!!,
    ibu = ibu!!
)