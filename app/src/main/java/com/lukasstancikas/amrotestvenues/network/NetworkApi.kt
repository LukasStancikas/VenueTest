package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.koin.NetworkModule.API_VERSION
import com.lukasstancikas.amrotestvenues.koin.NetworkModule.CLIENT_ID
import com.lukasstancikas.amrotestvenues.koin.NetworkModule.CLIENT_SECRET
import com.lukasstancikas.amrotestvenues.koin.NetworkModule.VENUE_RADIUS
import com.lukasstancikas.amrotestvenues.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {

    @GET("venues/search")
    fun getVenues(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET,
        @Query("ll") latLng: LatLng,
        @Query("limit") limit: Int,
        @Query("query") query: String?,
        @Query("v") version: String = API_VERSION,
        @Query("radius") radius: Int = VENUE_RADIUS
    ): Single<ForSquareResponse<VenueList>>

    @GET("venues/{venueId}")
    fun getVenueDetails(
        @Path("venueId") venueId: String,
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET,
        @Query("v") version: String = API_VERSION
    ): Single<ForSquareResponse<VenueDetails>>

    @GET("venues/{venueId}/photos")
    fun getVenuePhotos(
        @Path("venueId") venueId: String,
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET,
        @Query("v") version: String = API_VERSION
    ): Single<ForSquareResponse<VenuePhotos>>
}