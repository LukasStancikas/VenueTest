package com.lukasstancikas.amrotestvenues.model

data class Venues(val response: VenueResponse)
data class VenueResponse(val venues: List<Venue>)
data class Venue(val id: String, val name: String, val location: VenueLocation)
data class VenueLocation(val address: String, val city: String, val state: String)