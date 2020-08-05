package com.lukasstancikas.amrotestvenues.feature.venuedetails

import com.lukasstancikas.amrotestvenues.base.BaseViewModel
import com.lukasstancikas.amrotestvenues.extensions.scheduleOnBackgroundThread
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.network.VenueRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VenueDetailsViewModel(private val repo: VenueRepository) : BaseViewModel() {

    private val _venue = BehaviorSubject.create<Venue>()
    val venue: Observable<Venue> get() = _venue.hide()

    private val disposables = CompositeDisposable()


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun refreshVenue(id: String) {
        disposables.clear()
        repo
            .getVenuesDetails(id) { error ->
                error.localizedMessage?.let { _error.onNext(it) }
            }
            .scheduleOnBackgroundThread()
            .debounce(DB_DEBOUNCE_INTERVAL_MS, TimeUnit.MILLISECONDS)
            .withLoadingEvents()
            .subscribeBy(
                onNext = _venue::onNext,
                onError = Timber::e
            )
            .addTo(disposables)
    }

    companion object {
        private const val DB_DEBOUNCE_INTERVAL_MS = 500L
    }
}