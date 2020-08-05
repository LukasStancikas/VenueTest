package com.lukasstancikas.amrotestvenues.model

data class LatLng(val lat: Double, val lng: Double) {
    override fun toString(): String {
        return String.format("%.5f,%.5f", lat, lng)
    }
}