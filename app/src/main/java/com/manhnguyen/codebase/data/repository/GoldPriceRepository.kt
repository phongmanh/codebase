package com.manhnguyen.codebase.data.repository

import com.manhnguyen.codebase.data.api.ApiInterface
import com.manhnguyen.codebase.domain.model.GoldPriceModel
import com.manhnguyen.codebase.domain.usercase.IGoldPriceRepository
import retrofit2.Response

class GoldPriceRepository constructor (private val apiInterface: ApiInterface): IGoldPriceRepository {
    override suspend fun getGoldPrice(): Response<ArrayList<GoldPriceModel>> {
       return apiInterface.getGoldPrice()
    }
}