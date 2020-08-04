package com.lukasstancikas.amrotestvenues.feature.venuelist

import androidx.lifecycle.ViewModel
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.network.ApiController
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class VenuesViewModel(val api: ApiController) : ViewModel() {

    private val _venues = BehaviorSubject.create<List<Venue>>()
    val venues: Observable<List<Venue>> get() = _venues.hide()

    private val _query = BehaviorSubject.createDefault("")
    private val _latLng = BehaviorSubject.create<LatLng>()


    init {
        refreshVenues()
    }

    fun onNewQuery(query: String) {
        _query.onNext(query)
        refreshVenues()
    }

    fun refreshVenues() {
        _query
            .switchMap {
                api.getVenues()
            }
    }
}