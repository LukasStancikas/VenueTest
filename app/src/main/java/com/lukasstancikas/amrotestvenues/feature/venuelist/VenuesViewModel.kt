package com.lukasstancikas.amrotestvenues.feature.venuelist

import androidx.lifecycle.ViewModel
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.network.ApiController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class VenuesViewModel(val api: ApiController) : ViewModel() {

    private val _venues = BehaviorSubject.create<List<Venue>>()
    val venues: Observable<List<Venue>> get() = _venues.hide()

    private val _loading = BehaviorSubject.create<Boolean>()
    val loading: Observable<Boolean> get() = _loading.hide()

    private val _query = BehaviorSubject.createDefault("")
    private val _latLng = BehaviorSubject.create<Pair<Float, Float>>()

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

    fun onNewLocation(lat: Float, lng: Float) {
        _latLng.onNext(Pair(lat, lng))
    }

    fun refreshVenues() {
        _query
            .withLatestFrom(_latLng) { query, latLng ->
                VenueRequest(latLng.first, latLng.second, query)
            }
            .take(1)
            .singleElement()
            .flatMapSingle {
                api.getVenues(it.lat, it.lng, it.query)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loading.onNext(true) }
            .doFinally { _loading.onNext(false) }
            .subscribeBy(
                onSuccess = _venues::onNext,
                onError = Timber::e
            )
            .addTo(disposables)
    }

    data class VenueRequest(val lat: Float, val lng: Float, val query: String)
}