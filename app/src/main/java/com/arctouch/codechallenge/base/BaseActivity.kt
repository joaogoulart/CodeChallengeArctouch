package com.arctouch.codechallenge.base

import android.support.v7.app.AppCompatActivity
import com.arctouch.codechallenge.api.TmdbApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseActivity : AppCompatActivity() {

    protected val api: TmdbApi = Retrofit.Builder()
        .baseUrl(TmdbApi.URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TmdbApi::class.java)
}
