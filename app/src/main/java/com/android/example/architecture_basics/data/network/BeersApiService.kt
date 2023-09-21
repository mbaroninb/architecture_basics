package com.android.example.architecture_basics.data.network

import com.android.example.architecture_basics.data.network.models.BeerApi
import retrofit2.http.GET
import retrofit2.http.Path

interface BeersApiService {
    @GET("beers?per_page=80")
    suspend fun getBeers(): List<BeerApi>

    @GET("beers/{id}")
    suspend fun getBeerById(@Path("id")id: Int): List<BeerApi>
}