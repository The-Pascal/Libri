package com.example.libri.data.remote

import com.example.libri.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Network {

    private const val GOOGLE_BASE_URL = "https://www.googleapis.com/books/v1/"
    private const val NYT_BASE_URL = "https://api.nytimes.com/svc/books/v3/"
    private const val GUTENDEX_BASE_URL = "https://gutendex.com/"

    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofitBase by lazy {
        Retrofit.Builder()
            .baseUrl(GOOGLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val googleBooksApi: GoogleBooksApi by lazy {
        val client = okHttpClient.newBuilder()
            .addInterceptor(AuthInterceptor("key", BuildConfig.GOOGLE_API_KEY))
            .build()
        val retrofit = retrofitBase.newBuilder()
            .baseUrl(GOOGLE_BASE_URL)
            .client(client)
            .build()
        retrofit.create(GoogleBooksApi::class.java)
    }

    val nytBooksApi: NytBooksApi by lazy {
        val client = okHttpClient.newBuilder()
            .addInterceptor(AuthInterceptor("api-key", BuildConfig.NYT_API_KEY))
            .build()
        val retrofit = retrofitBase.newBuilder()
            .baseUrl(NYT_BASE_URL)
            .client(client)
            .build()
        retrofit.create(NytBooksApi::class.java)
    }

    val gutendexApi: GutendexApi by lazy {
        val client = okHttpClient.newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = retrofitBase.newBuilder()
            .baseUrl(GUTENDEX_BASE_URL)
            .client(client)
            .build()
        retrofit.create(GutendexApi::class.java)
    }

    class AuthInterceptor(private val apiKey: String, private val apiKeyValue: String): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            originalRequest.url

            val urlWithKey = originalRequest.url.newBuilder()
                .addQueryParameter(apiKey, apiKeyValue)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(urlWithKey)
                .build()

            return chain.proceed(newRequest)
        }
    }
}