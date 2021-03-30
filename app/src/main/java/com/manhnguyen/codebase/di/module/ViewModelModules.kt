package com.manhnguyen.codebase.di.module

import com.manhnguyen.codebase.presentation.main.MainViewModel
import com.manhnguyen.codebase.presentation.fragments.GoldPriceViewModel
import com.manhnguyen.codebase.system.locations.LocationViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModules {

    companion object {
        val modules = module {
            viewModel { LocationViewModel() }
            viewModel { GoldPriceViewModel(get()) }
            viewModel { MainViewModel(get(), get()) }
        }
    }

}