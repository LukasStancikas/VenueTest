package com.lukasstancikas.amrotestvenues.db.dao

import androidx.room.*
import com.lukasstancikas.amrotestvenues.db.VenueWithPhotos
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface VenueDao {
    @Query("SELECT * FROM Venue")
    fun getAll(): Observable<List<Venue>>

    @Query("SELECT * FROM Venue WHERE id = :id")
    fun getById(id: String): Observable<Venue>

    @Transaction
    @Query("SELECT * FROM Venue WHERE id = :id")
    fun getByIdWithPhotos(id: String): Observable<VenueWithPhotos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Venue>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Venue): Completable

    @Delete
    fun delete(items: List<Venue>): Completable
}