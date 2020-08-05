package com.lukasstancikas.amrotestvenues.koin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lukasstancikas.amrotestvenues.network.HeaderInterceptor
import com.lukasstancikas.amrotestvenues.network.NetworkApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object NetworkModule {

    fun get(): Module {
        return module {
            single { provideGson() }
            single { provideHttpLoggingInterceptor() }
            single { provideHeaderInterceptor() }
            single { provideOkHttpClient(get(), get()) }
            single { provideRetrofit(get(), get()) }
            single { provideApi(get()) }
        }
    }

    private fun provideApi(retrofit: Retrofit): NetworkApi = retrofit.create()

    private fun provideGson(): Gson = GsonBuilder()
        .create()

    private fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(API_URI)
        .client(okHttpClient)
        .build()

    private fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun provideHeaderInterceptor(): HeaderInterceptor = HeaderInterceptor()

    private const val TIMEOUT = 15L
    private const val API_URI = "https://api.foursquare.com/v2/"
    const val CLIENT_SECRET = "KYRFSGI4GXSAJ4OTWEAHQF5KK1UW2KEMQVMTMBHTILJWC2ZQ"
    const val CLIENT_ID = "BK2N0LZPFEBGLKTKS5UTZABOIGEYPYGIAQEZQOFUFON1VQJW"
    const val API_VERSION = "20200805"
    const val VENUE_RADIUS = 1000
}