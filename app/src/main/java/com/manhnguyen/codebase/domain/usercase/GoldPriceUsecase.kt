package com.manhnguyen.codebase.domain.usercase

import com.manhnguyen.codebase.domain.model.GoldPriceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GoldPriceUsecase constructor(private val repository: IGoldPriceRepository) {

    suspend fun getGoldPrice() = suspendCoroutine<ArrayList<GoldPriceModel>> { cont ->
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = repository.getGoldPrice()
                if (res.isSuccessful && !res.body().isNullOrEmpty()) {
                    cont.resume(res.body() as ArrayList<GoldPriceModel>)
                } else {
                    cont.resume(ArrayList())
                }
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }

}