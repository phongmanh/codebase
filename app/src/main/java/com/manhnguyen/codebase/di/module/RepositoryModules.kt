package com.manhnguyen.codebase.di.module

import com.manhnguyen.codebase.data.repository.GoldPriceRepository
import com.manhnguyen.codebase.domain.usercase.IGoldPriceRepository
import org.koin.dsl.binds
import org.koin.dsl.module

class RepositoryModules {

    companion object {
        val modules = module {
            factory { GoldPriceRepository(get()) } binds arrayOf(
                IGoldPriceRepository::class
            )
        }
    }

}