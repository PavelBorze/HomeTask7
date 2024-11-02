package com.pavelb.hometask.di

import android.content.Context
import com.pavelb.hometask.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkAPIClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_DEFAULT = 30L

    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun providesOkhttpClient(): OkHttpClient.Builder {
        val connectionPool = ConnectionPool()
        return OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .connectTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
    }

    //we simulate a real api
    @Provides
    @Named("BASE_URL")
    fun provideAppBaseUrl(): String = "https://hf-android-app.s3-eu-west-1.amazonaws.com"

    @NetworkAPIClient
    @Provides
    @Singleton
    fun providesReposRetrofitInstance(
        @ApplicationContext context: Context,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(provideAppBaseUrl())
            .addConverterFactory(providesConverterFactory())
            .client(
                providesOkhttpClient()
                    .appendLogs(context)
                    .build()
            )
            .build()
    }

}

private fun OkHttpClient.Builder.appendLogs(context: Context): OkHttpClient.Builder {
    if (BuildConfig.DEBUG) {
        addInterceptor(
            HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        )

    }
    return this
}