package com.example.random_quote_generator_app

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote():Response<List<QuoteModel>>
}