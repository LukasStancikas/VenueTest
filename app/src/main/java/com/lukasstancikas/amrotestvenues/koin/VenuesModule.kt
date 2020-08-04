package com.lukasstancikas.amrotestvenues.koin

import com.lukasstancikas.amrotestvenues.feature.venuelist.VenuesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object VenuesModule {
    fun get(): Module {
        return module {
            viewModel { VenuesViewModel(get()) }
        }
    }
}