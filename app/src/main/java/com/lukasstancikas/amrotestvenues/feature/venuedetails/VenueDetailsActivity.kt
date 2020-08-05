package com.lukasstancikas.amrotestvenues.feature.venuedetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.location.LocationRequest
import com.lukasstancikas.amrotestvenues.databinding.ActivityVenueDetailsBinding
import com.lukasstancikas.amrotestvenues.databinding.ActivityVenueListBinding
import com.lukasstancikas.amrotestvenues.extensions.ExtraNotNull
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenueAdapter
import com.patloew.rxlocation.RxLocation
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class VenueDetailsActivity : Activity() {

    private val venueId by ExtraNotNull(VENUE_ID_KEY,"")

    private val adapter = VenueAdapter()
    private val viewModel by viewModel<VenueDetailsViewModel>()
    private val disposables = CompositeDisposable()

    private lateinit var binding: ActivityVenueDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        adapter.onItemClick(::onItemClick)
    }

    companion object {
        const val VENUE_ID_KEY = "venueId"

        fun startActivity(activity: Activity, venueId: String) {
            val intent = Intent(activity, VenueDetailsActivity::class.java)
            intent.putExtra(VENUE_ID_KEY, venueId)
            activity.startActivity(intent)
        }
    }
}