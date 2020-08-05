package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Observable

interface VenueRepository {
    fun getVenues(latLng: LatLng, query: String, onNetworkError: ((Throwable) -> Unit)? = null): Observable<List<Venue>>

    fun getVenuesDetails(id: String, onNetworkError: ((Throwable) -> Unit)? = null): Observable<Venue>
}