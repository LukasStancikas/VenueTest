package com.lukasstancikas.amrotestvenues.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Venue(
    @PrimaryKey var id: String,
    var name: String,
    var description: String?,
    var rating: Double?,
    @Embedded var location: VenueLocation?,
    @Embedded var contact: VenueContact?,
    @Ignore var photos: VenueGroups?
) {

    // Needed because of @ignored field, but inside primary constructor for Gson
    constructor() : this("", "", "", null, null, null, null)

    fun getPhotoList(): List<VenuePhoto> {
        return photos?.groups?.flatMap { it.items }?.map { it.copy(venueId = this.id) }
            ?: emptyList()
    }
}