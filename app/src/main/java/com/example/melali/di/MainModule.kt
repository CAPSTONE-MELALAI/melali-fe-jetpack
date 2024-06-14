package com.example.melali.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ) = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideHttpClient(
        pref: SharedPreferences
    ) = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        pref.getString("token", "") ?: "",
                        ""
                    )
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 20000
            connectTimeoutMillis = 20000
            socketTimeoutMillis = 20000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HTTP Call", message)
                }
            }
            level = LogLevel.ALL
        }
    }
}