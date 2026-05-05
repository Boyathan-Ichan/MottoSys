package com.technologiesdesesperation.mottosys.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://TU_PROYECTO_ID.supabase.co/"

    private val gson = GsonBuilder()
        .serializeNulls()
        .create()

    val instance: SupabaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SupabaseApi::class.java)
    }
}