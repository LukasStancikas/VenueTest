package com.lukasstancikas.amrotestvenues

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lukasstancikas.amrotestvenues.databinding.ActivityMainBinding
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenueAdapter
import com.lukasstancikas.amrotestvenues.feature.venuelist.VenuesViewModel
import com.lukasstancikas.amrotestvenues.model.Venue
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val adapter = VenueAdapter()
    private val viewModel by viewModel<VenuesViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        adapter.onItemClick(::onItemClick)
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .venues
            .sub
    }

    private fun onItemClick(venue: Venue) {

    }
}
