package com.lukasstancikas.amrotestvenues.feature.venuedetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.lukasstancikas.amrotestvenues.R
import com.lukasstancikas.amrotestvenues.databinding.ActivityVenueDetailsBinding
import com.lukasstancikas.amrotestvenues.extensions.ExtraNotNull
import com.lukasstancikas.amrotestvenues.extensions.scheduleOnBackgroundThread
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenueAdapter
import com.lukasstancikas.amrotestvenues.model.Venue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class VenueDetailsActivity : AppCompatActivity() {

    private val venueId by ExtraNotNull(VENUE_ID_KEY, "")

    private val adapter = VenueAdapter()
    private val viewModel by viewModel<VenueDetailsViewModel>()
    private val disposables = CompositeDisposable()

    private lateinit var binding: ActivityVenueDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        viewModel.refreshVenue(venueId)
    }

    override fun onStart() {
        super.onStart()
        bindToViewModel()
        bindToViews()
    }

    private fun bindToViews() {
        binding
            .swipeRefresh
            .refreshes()
            .subscribeBy(
                onNext = { viewModel.refreshVenue(venueId) },
                onError = Timber::e
            )
            .addTo(disposables)
    }

    private fun bindToViewModel() {
        viewModel
            .venue
            .scheduleOnBackgroundThread()
            .subscribeBy(
                onNext = ::loadVenue,
                onError = Timber::e
            )
            .addTo(disposables)

        viewModel
            .loading
            .scheduleOnBackgroundThread()
            .subscribeBy(
                onNext = binding.swipeRefresh::setRefreshing,
                onError = Timber::e
            )
            .addTo(disposables)

        viewModel
            .error
            .scheduleOnBackgroundThread()
            .subscribeBy(
                onNext = { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG) },
                onError = Timber::e
            )
            .addTo(disposables)
    }

    private fun loadVenue(venue: Venue) {
        binding.name.text = venue.name
        binding.description.text = venue.description
        binding.rating.text = getString(R.string.label_rating, venue.rating)
        binding.address.text = venue.location.toString()
        val contacts = listOf(
            getString(R.string.label_phone, venue.contact?.formattedPhone),
            getString(R.string.label_twitter, venue.contact?.twitter),
            getString(R.string.label_instagram, venue.contact?.instagram)
        )
        binding.contacts.text = TextUtils.join("\n", contacts)
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