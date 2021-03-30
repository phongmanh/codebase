package com.manhnguyen.codebase.di.module

import com.manhnguyen.codebase.domain.usercase.GoldPriceUsecase
import org.koin.dsl.module

class UseCaseModule {
    companion object {
        val modules = module {
            single { GoldPriceUsecase(get()) }
        }
    }
}