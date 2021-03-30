package com.manhnguyen.codebase.data.api

import com.manhnguyen.codebase.domain.model.GoldPriceModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiInterface {

    @GET("/api/prices/chart_data")
    suspend fun getGoldPrice(
    ): Response<ArrayList<GoldPriceModel>>
}