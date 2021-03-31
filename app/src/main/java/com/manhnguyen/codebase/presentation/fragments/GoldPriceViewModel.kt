package com.manhnguyen.codebase.presentation.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manhnguyen.codebase.domain.model.GoldPriceModel
import com.manhnguyen.codebase.domain.model.ProfileModel
import com.manhnguyen.codebase.domain.usercase.GoldPriceUsecase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GoldPriceViewModel(private val usecase: GoldPriceUsecase) : ViewModel() {

    private val goldPriceDateLive = MutableLiveData<ArrayList<GoldPriceModel>>()

    fun getGoldPrice(): LiveData<ArrayList<GoldPriceModel>> = goldPriceDateLive
    fun loadGoldPrice() {
        viewModelScope.launch {
            try {
                val data = async { usecase.getGoldPrice() }
                goldPriceDateLive.postValue(data.await())
            } catch (e: Exception) {
                goldPriceDateLive.postValue(ArrayList())
            }

        }
    }

    fun getProfile(): ProfileModel {
        return ProfileModel("phongmanhvn@gmail.com", "Manh Nguyen Huu")
    }
}