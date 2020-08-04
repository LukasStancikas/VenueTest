package com.lukasstancikas.amrotestvenues

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationRequest
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.lukasstancikas.amrotestvenues.databinding.ActivityMainBinding
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenueAdapter
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenuesViewModel
import com.lukasstancikas.amrotestvenues.model.Venue
import com.patloew.rxlocation.RxLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.IllegalStateException


class MainActivity : AppCompatActivity() {

    private val adapter = VenueAdapter()
    private val viewModel by viewModel<VenuesViewModel>()
    private val disposables = CompositeDisposable()
    private val rxLocation by lazy { RxLocation(this) }


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        adapter.onItemClick(::onItemClick)
    }

    override fun onStart() {
        super.onStart()
        bindToLocation()
        bindToViewModel()
        bindToViews()
    }

    private fun bindToLocation() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(15000)
        // TODO fix hardcoded 15000
        rxLocation
            .settings()
            .checkAndHandleResolution(locationRequest)
            .flatMapObservable { granted ->
                if (granted) {
                    rxLocation.location().updates(locationRequest).map {
                        Pair(it.latitude, it.longitude)
                    }
                } else {
                    throw IllegalStateException()
                }
            }
    }

    private fun bindToViews() {
        binding
            .swipeRefresh
            .refreshes()
            .subscribeBy(
                onNext = { viewModel.refreshVenues() },
                onError = Timber::e
            )
            .addTo(disposables)

        binding
            .search
            .queryTextChanges()
            .skipInitialValue()
            .subscribeBy(
                onNext = { viewModel.onNewQuery(it.toString()) },
                onError = Timber::e
            )
            .addTo(disposables)
    }

    private fun bindToViewModel() {
        viewModel
            .venues
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = adapter::submitList,
                onError = Timber::e
            )
            .addTo(disposables)

        viewModel
            .loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = binding.swipeRefresh::setRefreshing,
                onError = Timber::e
            )
            .addTo(disposables)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun onItemClick(venue: Venue) {

    }
}
