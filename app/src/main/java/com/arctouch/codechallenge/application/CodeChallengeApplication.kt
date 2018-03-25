package com.arctouch.codechallenge.application

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.common.constants.ApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Joao on 25/03/2018.
 */
class CodeChallengeApplication: Application() {

    val api: TmdbApi = Retrofit.Builder()
            .baseUrl(ApiConstants.API_URL)
            .client(OkHttpClient.Builder().addInterceptor { interceptorChain ->
                var request = interceptorChain.request()
                val url = request.url().newBuilder()
                        .addQueryParameter(ApiConstants.API_KEY_HEADER, ApiConstants.API_KEY)
                        .addQueryParameter(ApiConstants.LANGUAGE_HEADER, ApiConstants.DEFAULT_LANGUAGE)
                        .build()
                request = request.newBuilder().url(url).build()
                interceptorChain.proceed(request)
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    companion object {
        lateinit var instance: CodeChallengeApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}