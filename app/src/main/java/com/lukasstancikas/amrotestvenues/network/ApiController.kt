package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Single

interface ApiController {
    fun getVenues(lat: Float, lng: Float, query: String): Single<List<Venue>>
}