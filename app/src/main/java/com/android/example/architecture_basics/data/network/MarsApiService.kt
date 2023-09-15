package com.android.example.architecture_basics.data.network

import com.android.example.architecture_basics.data.network.models.MarsPhoto
import retrofit2.http.GET

interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>

}