package com.lukasstancikas.amrotestvenues.koin

import androidx.room.Room
import com.google.android.gms.location.LocationRequest
import com.lukasstancikas.amrotestvenues.db.AppDatabase
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenuesViewModel
import com.patloew.rxlocation.RxLocation
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object VenuesModule {
    fun get(): Module {
        return module {
            viewModel { VenuesViewModel(get()) }

            single {
                LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(LOCATION_INTERVAL_MS)
            }

            single {
                RxLocation(get())
            }

            single {
                Room
                    .databaseBuilder(
                        get(),
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                    .build()
            }
        }
    }

    private const val LOCATION_INTERVAL_MS = 8000L
    private const val DATABASE_NAME = "venues-db"
}