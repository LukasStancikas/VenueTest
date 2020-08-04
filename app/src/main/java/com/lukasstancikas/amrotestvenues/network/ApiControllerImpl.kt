package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.koin.NetworkModule
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Single

class ApiControllerImpl(
    private val api: NetworkApi
) : ApiController {
    override fun getVenues(lat: Float, lng: Float, query: String): Single<List<Venue>> {
        val latLng = "$lat,$lng"
        val limit = 10
        val apiCall = api
            .getVenues(
                NetworkModule.CLIENT_ID,
                NetworkModule.CLIENT_SECRET,
                latLng,
                limit,
                query
            )
            .map {
                it.response.venues
            }
        // do check from room db
        return apiCall
    }
}