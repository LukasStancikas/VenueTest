package com.lukasstancikas.amrotestvenues.network

import android.annotation.SuppressLint
import com.lukasstancikas.amrotestvenues.db.AppDatabase
import com.lukasstancikas.amrotestvenues.model.LatLng
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class VenueRepositoryImpl(
    private val api: NetworkApi,
    private val db: AppDatabase
) : VenueRepository {
    override fun getVenues(latLng: LatLng, query: String): Observable<List<Venue>> =
        Observable.concatArray(
            db.venueDao().getAllWithQuery(QUERY_LIMIT, query),
            getVenuesFromApi(latLng, QUERY_LIMIT, query)
        )

    private fun getVenuesFromApi(latLng: LatLng, limit: Int, query: String) = api
        .getVenues(
            latLng = latLng,
            limit = limit,
            query = query.takeIf { it.isNotEmpty() }
        )
        .map {
            it.response.venues
        }
        .toObservable()
        .doOnNext(::addVenuesToDb)

    override fun getVenuesDetails(id: String): Single<Venue> {
        return api.getVenueDetails(id).map { it.response.venue }
    }

    @SuppressLint("CheckResult")
    // We do not care about the result of async insertion
    private fun addVenuesToDb(venues: List<Venue>) {
        db.venueDao()
            .updateAll(venues)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { Timber.d("added ${venues.size} to DB") },
                onError = Timber::e
            )
    }

    companion object {
        private const val QUERY_LIMIT = 10
    }
}