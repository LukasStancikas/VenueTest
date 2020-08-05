package com.lukasstancikas.amrotestvenues.app

import android.app.Application
import com.lukasstancikas.amrotestvenues.koin.NetworkModule
import com.lukasstancikas.amrotestvenues.koin.RepositoryModule
import com.lukasstancikas.amrotestvenues.koin.VenuesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class VenuesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin()
        Timber.plant(Timber.DebugTree())
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger()
            androidContext(this@VenuesApplication)

            modules(
                listOf(
                    VenuesModule.get(),
                    NetworkModule.get(),
                    RepositoryModule.get()
                )
            )
        }
    }
}