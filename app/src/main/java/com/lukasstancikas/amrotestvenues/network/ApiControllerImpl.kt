package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.koin.NetworkModule
import com.lukasstancikas.amrotestvenues.koin.NetworkModule.API_VERSION
import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Single

class ApiControllerImpl(
    private val api: NetworkApi
) : ApiController {
    override fun getVenues(latLng: LatLng, query: String): Single<List<Venue>> {
        val limit = 10
        val apiCall = api
            .getVenues(
                latLng = latLng,
                limit = limit,
                query = query.takeIf { it.isNotEmpty() },
            )
            .map {
                it.response.venues
            }
        // TODO do check from room db
        return apiCall
    }
}