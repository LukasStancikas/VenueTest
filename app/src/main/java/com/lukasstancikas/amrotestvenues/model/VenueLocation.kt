package com.lukasstancikas.amrotestvenues.model

data class VenueLocation(val address: String?, val city: String?, val state: String?) {
    override fun toString(): String {
        val optionalAddress = if (!address.isNullOrEmpty()) "$address\n" else ""
        return "$optionalAddress$city, $state"
    }
}