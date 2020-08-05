package com.lukasstancikas.amrotestvenues.feature.venuelist

import com.lukasstancikas.amrotestvenues.base.BaseViewModel
import com.lukasstancikas.amrotestvenues.extensions.scheduleOnBackgroundThread
import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.network.VenueRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VenuesViewModel(private val repo: VenueRepository) : BaseViewModel() {

    private val _venues = BehaviorSubject.create<List<Venue>>()
    val venues: Observable<List<Venue>> get() = _venues.hide()

    private val _query = BehaviorSubject.createDefault("")
    private val _latLng = BehaviorSubject.create<LatLng>()

    private val disposables = CompositeDisposable()

    init {
        refreshVenues()
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun onNewQuery(query: String) {
        _query.onNext(query)
        refreshVenues()
    }

    fun onNewLocation(lat: Double, lng: Double) {
        _latLng.onNext(LatLng(lat, lng))
    }

    fun refreshVenues() {
        disposables.clear()
        Observables.combineLatest(
            _latLng,
            _query
        )
            .take(1)
            .singleElement()
            .withLoadingEvents()
            .flatMapObservable {
                repo
                    .getVenues(it.first, it.second) {error ->
                        error.localizedMessage?.let { _error.onNext(it) }
                    }
                    .scheduleOnBackgroundThread()
                    .distinctUntilChanged()
                    .debounce(DB_DEBOUNCE_INTERVAL_MS, TimeUnit.MILLISECONDS)
            }
            .subscribeBy(
                onNext = _venues::onNext,
                onError = Timber::e
            )
            .addTo(disposables)
    }

    companion object {
        private const val DB_DEBOUNCE_INTERVAL_MS = 500L
    }
}