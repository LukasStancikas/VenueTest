package com.lukasstancikas.amrotestvenues.network

import android.annotation.SuppressLint
import com.lukasstancikas.amrotestvenues.db.AppDatabase
import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class VenueRepositoryImpl(
    private val api: NetworkApi,
    private val db: AppDatabase
) : VenueRepository {
    override fun getVenues(
        latLng: LatLng,
        query: String,
        onNetworkError: (Throwable) -> Unit
    ): Observable<List<Venue>> {
        val dbCall = db
            .venueDao()
            .getAll()
            .map { it.filter { it.name.toLowerCase().contains(query.toLowerCase()) } }

        return Observable.merge(
            dbCall,
            getVenuesFromApi(latLng, QUERY_LIMIT, query, onNetworkError)
        )
    }

    private fun getVenuesFromApi(
        latLng: LatLng,
        limit: Int,
        query: String,
        onNetworkError: (Throwable) -> Unit
    ): Observable<List<Venue>> {
        return api
            .getVenues(
                latLng = latLng,
                limit = limit,
                query = query.takeIf { it.isNotEmpty() }
            )
            .map {
                it.response.venues
            }
            .doOnSuccess(::addVenuesToDb)
            .doOnError(onNetworkError)
            .toObservable()
            .materialize()
            .filter { !it.isOnError }
            .dematerialize()
    }

    override fun getVenuesDetails(
        id: String,
        onNetworkError: (Throwable) -> Unit
    ): Observable<Venue> {
        val apiCall = api
            .getVenueDetails(id)
            .map { it.response.venue }
            .doOnSuccess(::addVenueToDb)
            .doOnError(onNetworkError)
            .toObservable()
            .materialize()
            .filter { !it.isOnError }
            .dematerialize<Venue>()

        return Observable.merge(
            db.venueDao().getById(id),
            apiCall
        )
    }

    @SuppressLint("CheckResult")
    // We do not care about the result of async insertion
    private fun addVenuesToDb(venues: List<Venue>) {
        db.venueDao()
            .insertAll(venues)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { Timber.d("added ${venues.size} to DB") },
                onError = Timber::e
            )
    }

    @SuppressLint("CheckResult")
    // We do not care about the result of async insertion
    private fun addVenueToDb(venue: Venue) {
        db.venueDao()
            .insert(venue)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { Timber.d("added \"${venue.name}\" details to DB") },
                onError = Timber::e
            )
    }

    companion object {
        private const val QUERY_LIMIT = 10
    }
}