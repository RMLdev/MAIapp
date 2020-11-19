package com.rml.maiapp.utils

import com.google.gson.Gson
import com.rml.maiassistant.BuildConfig
import com.rml.maiassistant.data.api.MAIApi
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MaiApiUtils {

    private fun getClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
        if (BuildConfig.BUILD_TYPE != "release") {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }

    private fun getRetrofit(): Retrofit {
        val gson = Gson()
        return Retrofit.Builder()
            .baseUrl("https://wrapapi.com/use/RML/maischeduleapp/")
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    fun getApiService(): MAIApi {
        return getRetrofit().create(MAIApi::class.java)
    }
}