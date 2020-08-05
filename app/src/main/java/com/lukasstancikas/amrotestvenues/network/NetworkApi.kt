package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.koin.NetworkModule.API_VERSION
import com.lukasstancikas.amrotestvenues.koin.NetworkModule.CLIENT_ID
import com.lukasstancikas.amrotestvenues.koin.NetworkModule.CLIENT_SECRET
import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venues
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("venues/search")
    fun getVenues(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET,
        @Query("ll") latLng: LatLng,
        @Query("limit") limit: Int,
        @Query("query") query: String?,
        @Query("v") version: String = API_VERSION
    ): Single<Venues>
}