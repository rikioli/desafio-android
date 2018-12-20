package br.com.henriqueoliveira.desafioandroidconcrete.di

import androidx.test.espresso.IdlingRegistry
import br.com.henriqueoliveira.desafioandroidconcrete.BuildConfig
import br.com.henriqueoliveira.desafioandroidconcrete.helpers.IdleTest
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.GitHubService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT = 120L

val remoteDatasourceModule = module(definition = {
    single { createOkHttpClient() }
    single { createWebService<GitHubService>(get(), BuildConfig.BASE_URL) }
})

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {

    IdlingRegistry.getInstance().register(IdleTest.create(
            "okhttp", okHttpClient))


    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()
    return retrofit.create(T::class.java)
}