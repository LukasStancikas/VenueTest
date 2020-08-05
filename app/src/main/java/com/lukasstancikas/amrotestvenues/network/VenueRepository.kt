package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface VenueRepository {
    fun getVenues(latLng: LatLng, query: String): Observable<List<Venue>>

    fun getVenuesDetails(id: String): Single<Venue>
}