package com.lukasstancikas.amrotestvenues.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Venue(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val rating: Double,
    @Embedded val location: VenueLocation,
    @Embedded val contact: VenueContact?
)