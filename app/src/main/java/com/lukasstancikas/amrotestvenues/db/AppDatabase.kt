package com.lukasstancikas.amrotestvenues.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasstancikas.amrotestvenues.db.dao.PhotosDao
import com.lukasstancikas.amrotestvenues.db.dao.VenueDao
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.model.VenuePhoto

@Database(entities = [Venue::class, VenuePhoto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao

    abstract fun photosDao(): PhotosDao
}