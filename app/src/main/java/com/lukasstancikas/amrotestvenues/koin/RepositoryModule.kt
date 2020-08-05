package com.lukasstancikas.amrotestvenues.koin

import androidx.room.Room
import com.lukasstancikas.amrotestvenues.db.AppDatabase
import com.lukasstancikas.amrotestvenues.network.VenueRepository
import com.lukasstancikas.amrotestvenues.network.VenueRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object RepositoryModule {
    fun get(): Module {
        return module {
            single {
                Room
                    .databaseBuilder(
                        get(),
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                    .build()
            }

            single<VenueRepository> { VenueRepositoryImpl(get(), get()) }
        }
    }

    private const val DATABASE_NAME = "venues-db"
}