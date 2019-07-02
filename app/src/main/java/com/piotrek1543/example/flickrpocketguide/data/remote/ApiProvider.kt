package com.piotrek1543.example.flickrpocketguide.data.remote

import com.piotrek1543.example.flickrpocketguide.BuildConfig
import com.piotrek1543.example.flickrpocketguide.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiProvider {
    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    //OkhttpClient for building http request url
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .build()


    private fun retrofit(): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()


    val flickrApi: FlickrApi = retrofit().create(FlickrApi::class.java)

}