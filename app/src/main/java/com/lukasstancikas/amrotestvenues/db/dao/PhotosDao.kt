package com.lukasstancikas.amrotestvenues.db.dao

import androidx.room.*
import com.lukasstancikas.amrotestvenues.db.VenueWithPhotos
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.model.VenuePhoto
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<VenuePhoto>): Completable

    @Delete
    fun delete(items: List<VenuePhoto>): Completable
}