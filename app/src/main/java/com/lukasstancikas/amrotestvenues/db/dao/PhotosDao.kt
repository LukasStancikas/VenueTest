package com.lukasstancikas.amrotestvenues.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.lukasstancikas.amrotestvenues.model.VenuePhoto
import io.reactivex.Completable

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<VenuePhoto>): Completable

    @Delete
    fun delete(items: List<VenuePhoto>): Completable
}