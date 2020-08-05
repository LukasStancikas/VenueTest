package com.lukasstancikas.amrotestvenues.db.dao

import androidx.room.*
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface VenueDao {
    @Query("SELECT * FROM Venue")
    fun getAll(): Observable<List<Venue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Venue>): Completable

    @Delete
    fun delete(items: List<Venue>): Completable
}