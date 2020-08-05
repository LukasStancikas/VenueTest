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
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.lukasstancikas.amrotestvenues.R
import com.lukasstancikas.amrotestvenues.databinding.ActivityMainBinding
import com.lukasstancikas.amrotestvenues.databinding.ActivityVenueListBinding
import com.lukasstancikas.amrotestvenues.extensions.scheduleOnBackgroundThread
import com.lukasstancikas.amrotestvenues.model.Venue
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
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

    @SuppressLint("MissingPermission")
    // Permission check is not missing, checkLocationSettingsAndPermissions() checks it inside
    private fun bindToLocation() {
        checkLocationSettingsAndPermissions {
            rxLocation
                .location()
                .updates(locationRequest)
                .map {
                    Pair(it.latitude, it.longitude)
                }
                .scheduleOnBackgroundThread()
                .subscribeBy(
                    onNext = { viewModel.onNewLocation(it.first, it.second) },
                    onError = Timber::e
                )
                .addTo(disposables)
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
                onNext = { viewModel.refreshVenues() },
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

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun onItemClick(venue: Venue) {

    }


    companion object {
        private const val SEARCH_DEBOUNCE_INTERVAL_MS = 1000L
    }
}
