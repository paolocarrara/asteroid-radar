package com.udacity.asteroidradar.api

import okhttp3.OkHttpClient

class HttpClient: OkHttpClient() {

    companion object {
        // TODO Insert your key
        const val API_KEY = ""

        fun getClient(): OkHttpClient {
            return Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url()
                        .newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }
                .build()
        }
    }
}