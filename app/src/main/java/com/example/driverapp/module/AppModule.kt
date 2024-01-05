package com.example.driverapp.module

import com.example.driverapp.network.DriverAppApi
import com.example.driverapp.repository.TripRepository
import com.example.driverapp.repository.TripRepositoryImpl
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.ZonedDateTime

val appModule = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideForecastApi(get()) }
    single { provideRetrofit(get()) }
    single<TripRepository> { TripRepositoryImpl(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gson = GsonBuilder().registerTypeAdapter(
            LocalDateTime::class.java,
            JsonDeserializer { json, _, _ ->
                ZonedDateTime.parse(json.asJsonPrimitive.asString).toLocalDateTime()
            } as JsonDeserializer<LocalDateTime?>
        ).create()
    return Retrofit.Builder().baseUrl("https://storage.googleapis.com/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val client = OkHttpClient().newBuilder()
    if (DEBUG) {
        client.addInterceptor(httpLoggingInterceptor)
    }
    return client.build()
}

fun provideForecastApi(retrofit: Retrofit): DriverAppApi =
    retrofit.create(DriverAppApi::class.java)

const val DEBUG = true