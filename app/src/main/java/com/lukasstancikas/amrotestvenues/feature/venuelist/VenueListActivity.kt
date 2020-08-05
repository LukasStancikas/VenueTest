package com.lukasstancikas.amrotestvenues.feature.venuelist

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog.BUTTON_POSITIVE
import android.app.AlertDialog.Builder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.lukasstancikas.amrotestvenues.R
import com.lukasstancikas.amrotestvenues.databinding.ActivityVenueListBinding
import com.lukasstancikas.amrotestvenues.extensions.scheduleOnBackgroundThread
import com.lukasstancikas.amrotestvenues.feature.venuedetails.VenueDetailsActivity
import com.lukasstancikas.amrotestvenues.model.Venue
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VenueListActivity : AppCompatActivity() {

    private val adapter = VenueAdapter()
    private val viewModel by viewModel<VenuesViewModel>()
    private val locationRequest by inject<LocationRequest>()
    private val rxLocation by inject<RxLocation>()
    private val disposables = CompositeDisposable()
    private var rxLocationDisposable: Disposable? = null

    private lateinit var binding: ActivityVenueListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        adapter.onItemClick(::onItemClick)
    }

    override fun onStart() {
        super.onStart()
        bindToLocation()
        bindToViewModel()
        bindToViews()
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
        rxLocationDisposable?.dispose()
    }

    @SuppressLint("MissingPermission")
    // Permission check is not missing, checkLocationSettingsAndPermissions() checks it inside
    private fun bindToLocation() {
        checkLocationSettingsAndPermissions {
            rxLocationDisposable?.dispose()
            rxLocationDisposable = rxLocation
                .location()
                .updates(locationRequest)
                .scheduleOnBackgroundThread()
                .subscribeBy(
                    onNext = { viewModel.onNewLocation(it.latitude, it.longitude) },
                    onError = Timber::e
                )
        }
    }

    @SuppressLint("CheckResult")
    private fun checkLocationSettingsAndPermissions(onSuccess: () -> Unit) {
        // not adding to disposables since dialogs will call Activity onStop
        rxLocation
            .settings()
            .checkAndHandleResolution(locationRequest)
            .flatMapObservable { enabled ->
                if (enabled) {
                    RxPermissions(this)
                        .requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    throw IllegalStateException()
                }
            }
            .scheduleOnBackgroundThread()
            .doOnError {
                binding.swipeRefresh.isRefreshing = false
            }
            .subscribeBy(
                onNext = { permission ->
                    when {
                        permission.granted                              -> onSuccess()
                        permission.shouldShowRequestPermissionRationale -> showLocationRationale()
                        else                                            -> showLocationPermissionDeniedDialog()
                    }
                },
                onError = Timber::e
            )
    }

    private fun bindToViews() {
        binding
            .swipeRefresh
            .refreshes()
            .subscribeBy(
                onNext = {
                    bindToLocation()
                    viewModel.refreshVenues()
                },
                onError = Timber::e
            )
            .addTo(disposables)

        binding
            .search
            .queryTextChanges()
            .skipInitialValue()
            .debounce(SEARCH_DEBOUNCE_INTERVAL_MS, TimeUnit.MILLISECONDS)
            .subscribeBy(
                onNext = { viewModel.onNewQuery(it.toString()) },
                onError = Timber::e
            )
            .addTo(disposables)
    }

    private fun bindToViewModel() {
        viewModel
            .venues
            .scheduleOnBackgroundThread()
            .subscribeBy(
                onNext = adapter::submitList,
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
                onNext = { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() },
                onError = Timber::e
            )
            .addTo(disposables)
    }

    private fun showLocationRationale() {
        val alert = Builder(this).create()
        alert.setTitle(R.string.location_rationale_title)
        alert.setMessage(getString(R.string.location_rationale_description))
        alert.setButton(BUTTON_POSITIVE, getString(android.R.string.ok)) { dialog, which ->
            bindToLocation()
            dialog.dismiss()
        }
        alert.show()
    }

    private fun showLocationPermissionDeniedDialog() {
        val alert = Builder(this).create()
        alert.setTitle(R.string.location_permission_denied_title)
        alert.setMessage(getString(R.string.location_permission_denied_description))
        alert.setButton(BUTTON_POSITIVE, getString(android.R.string.ok)) { dialog, which ->
            goToSettings()
            dialog.dismiss()
        }
        alert.show()
    }

    private fun goToSettings() {
        val myAppSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(myAppSettings)
    }

    private fun onItemClick(venue: Venue) {
        VenueDetailsActivity.startActivity(this, venue.id)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_INTERVAL_MS = 1000L
    }
}
