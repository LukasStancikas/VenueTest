package com.lukasstancikas.amrotestvenues.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class VenuePhotos(val photos: List<VenuePhoto>)

data class VenueGroups(val groups: List<PhotoGroup>)
data class PhotoGroup(val items: List<VenuePhoto>)

@Entity
data class VenuePhoto(@PrimaryKey val id: String, val venueId: String, val prefix: String, val suffix: String, val width: Int, val height: Int) {
    override fun toString(): String {
        return "$prefix${width}x$height$suffix"
    }
}
