package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.model.Venues
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {
    @GET("venues/search")
    fun getVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("ll") latLng: String,
        @Query("limit") limit: Int,
        @Query("query") query: String
    ): Single<Venues>
}