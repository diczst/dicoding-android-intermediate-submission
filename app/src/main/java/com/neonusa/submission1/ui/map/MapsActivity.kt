package com.neonusa.submission1.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.neonusa.submission1.R
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.databinding.ActivityMapsBinding
import com.techiness.progressdialoglibrary.ProgressDialog
import org.koin.android.ext.android.inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()
    private val viewModel: MapsViewModel by inject()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.storiesLocations().observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    progressDialog.dismiss()
                    val data = it.data ?: emptyList()

                    // tambahkan banyak marker
                    addManyMarker(data)
                }
                State.ERROR -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, getString(R.string.fail_get_data), Toast.LENGTH_SHORT).show()
                }
                State.LOADING -> {
                    progressDialog.show()
                }
            }
        }

    }

    private fun setupToolbar() {
        binding.toolbarHome.toolbarMain.apply {
            inflateMenu(R.menu.map_options)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.normal_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        true
                    }
                    R.id.satellite_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        true
                    }
                    R.id.terrain_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        true
                    }
                    R.id.hybrid_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        true
                    }
                    else -> {
                        super.onOptionsItemSelected(item)
                    }
                }
            }
        }
    }

    private fun addManyMarker(storiesLocations: List<Story>) {
        storiesLocations.forEach { story ->
            val latLng = LatLng(story.lat!!.toDouble(), story.lon!!.toDouble())
            mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}