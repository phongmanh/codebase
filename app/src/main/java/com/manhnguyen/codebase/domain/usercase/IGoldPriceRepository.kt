package com.manhnguyen.codebase.domain.usercase
import com.manhnguyen.codebase.domain.model.GoldPriceModel
import retrofit2.Response

interface IGoldPriceRepository {
    suspend fun getGoldPrice(): Response<ArrayList<GoldPriceModel>>
}