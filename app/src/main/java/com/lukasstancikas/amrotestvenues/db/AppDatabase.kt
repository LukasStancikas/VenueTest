package com.lukasstancikas.amrotestvenues.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasstancikas.amrotestvenues.db.dao.VenueDao
import com.lukasstancikas.amrotestvenues.model.Venue

@Database(entities = arrayOf(Venue::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}