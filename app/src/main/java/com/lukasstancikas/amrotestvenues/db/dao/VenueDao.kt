package com.lukasstancikas.amrotestvenues.db.dao

import androidx.room.*
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface VenueDao {
//    @Query("SELECT * FROM venue")
//    fun getAll(): Observable<List<Venue>>

    @Query("SELECT * FROM venue WHERE venue.name LIKE :query LIMIT :limit")
    fun getAllWithQuery(limit: Int, query: String): Observable<List<Venue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Venue>): Completable

    @Update
    fun updateAll(items: List<Venue>): Completable

    @Delete
    fun delete(items: List<Venue>): Completable
}