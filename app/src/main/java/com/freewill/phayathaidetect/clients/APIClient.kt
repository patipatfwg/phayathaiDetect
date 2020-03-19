package com.fg.mdp.fwgfacilitiesfinder.clients

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {


    companion object {

        val baseURL: String = "http://freewillmdc.loginto.me:56870"
        // val baseURL: String = "https://mdcforheatstroke.000webhostapp.com"
        val baseURLWEB: String = "https://hr.freewillgroup.com"
        var retofit: Retrofit? = null

        var gson = GsonBuilder()
            .setLenient()
            .create()

        val clientTimeout = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val client = Retrofit.Builder()
            .baseUrl(baseURL)
            //.client(get.getUnsafeOkHttpClient())
            // .client(clientTimeout)

            //.addConverterFactory(ProtoConverterFactory)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiInterface::class.java!!)







//        val client: Retrofit
//            get() {
//
//                if (retofit == null) {
//                    retofit = Retrofit.Builder()
//                        .baseUrl(baseURL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build()
//                }
//                return retofit!!
//            }
    }

}