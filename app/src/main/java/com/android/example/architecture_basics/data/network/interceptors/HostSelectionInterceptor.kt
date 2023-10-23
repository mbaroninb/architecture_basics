package com.android.example.architecture_basics.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/*
 * Created by NaRan on 9/2/21, 3:44 PM
 * Last modified 9/2/21, 3:44 PM
 * Copyright (c) 2021 .
 * All rights reserved.
 *
 */

/*
  Solution from @swankjesse
  Host Selection Retrofit
  More at @link https://github.com/square/retrofit/issues/1404#issuecomment-207408548
*/
class HostSelectionInterceptor : Interceptor {

    private var host = ""

    fun setHostBaseUrl(string: String) {
        host = string
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        val newUrl = request.url.newBuilder()
            .host(host)
            .build()

        request = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }

}