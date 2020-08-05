package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Single

interface ApiController {
    fun getVenues(latLng: LatLng, query: String): Single<List<Venue>>
}