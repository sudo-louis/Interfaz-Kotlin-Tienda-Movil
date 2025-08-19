package com.example.tiendamovil.net

import android.content.Context
import com.example.tiendamovil.Const
import com.example.tiendamovil.Prefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun api(ctx: Context): ApiService {
        val prefs = Prefs(ctx)

        // Interceptor para headers requeridos por Laravel API
        val headersInterceptor = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            // Si hay token, agregar Bearer
            prefs.getToken()?.let { t ->
                builder.addHeader("Authorization", "Bearer $t")
            }

            chain.proceed(builder.build())
        }

        val log = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(log)
            .build()

        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL) // ej: http://10.0.2.2:8000/api/
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
