package com.lukasstancikas.amrotestvenues.db

import androidx.room.Embedded
import androidx.room.Relation
import com.lukasstancikas.amrotestvenues.model.Venue
import com.lukasstancikas.amrotestvenues.model.VenuePhoto

data class VenueWithPhotos(
    @Embedded val venue: Venue,
    @Relation(
        parentColumn = "id",
        entityColumn = "venueId"
    )
    val photos: List<VenuePhoto>
)