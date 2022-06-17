package pt.ulusofona.deisi.cm2122.g21700980_21906966.map

import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.NavigationManager
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentMapBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.list.FireListAdapter
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosViewModel
import java.util.*

class MapFragment : Fragment(), OnLocationChangedListener {

    private lateinit var binding: FragmentMapBinding
    private val repo = FogosRepository.getInstance()
    private var viewmodel = FogosViewModel()
    private var adapter = FireListAdapter(onClick = ::onFireClick, onLongClick = ::onFireLongClick)

    // mapa
    private lateinit var geocoder: Geocoder
    private var district = "Lisboa"
    private var map: GoogleMap? = null

    // risk
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.map)

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        geocoder = Geocoder(context, Locale.getDefault())
        binding = FragmentMapBinding.bind(view)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync {
            map = it
            FusedLocation.registerListener(this)
        }

        viewmodel = ViewModelProvider(this)[FogosViewModel::class.java]
        viewmodel.onGetFireList { updateFireList(it) }

        Handler(Looper.getMainLooper()).postDelayed({ drawMarker(39.74, -8.8, "Leiria Não Existe") }, 2000)
        Handler(Looper.getMainLooper()).postDelayed({ drawFogosOnMap() }, 1000)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        updateRisk()

        viewmodel.onGetFireList { updateFireList(it) }
        Handler(Looper.getMainLooper()).postDelayed({ drawFogosOnMap() }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({ drawMarker(39.74, -8.8, "Leiria Não Existe") }, 2000)
    }

    // RISK
    fun updateRisk() {
        // change risk message
        binding.risk.postDelayed(Runnable {
            binding.risk.postDelayed(runnable, 5000)
            binding.risk.text = repo.getRisk(district)

            Log.i("RISCO", binding.risk.text.toString())
            // mudar cor
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127, 127, 127)
                binding.risk.setTextColor(gray)
            } else {
                val cor = when (binding.risk.text) {
                    "Reduzido" -> "#4d87e3"
                    "Moderado" -> "#46a112"
                    "Elevado" -> "#f7dd72"
                    "Muito Elevado" -> "#e34814"
                    "Máximo" -> "#da291c"
                    else -> "#46a112"
                }

                binding.risk.setTextColor(Color.parseColor(cor))
            }

        }.also { runnable = it }, 0)
    }

    // LOCATION
    override fun onLocationChanged(latitude: Double, longitude: Double) {
        placeCityName(latitude, longitude)
        drawMarker(latitude, longitude, "Localização atual")
        Handler(Looper.getMainLooper()).postDelayed({
//            placeCamera(latitude, longitude)
        }, 2000)
    }

    private fun placeCamera(latitude: Double, longitude: Double) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(11f)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun placeCityName(latitude: Double, longitude: Double): String {
        val addresses = geocoder.getFromLocation(latitude, longitude, 5)
        val location = addresses.first { it.adminArea != null && it.adminArea.isNotEmpty() }
        binding.tvCityName.text = location.adminArea
        district = location.adminArea.split(" ")[0]
        return location.adminArea
    }

    // ICONS
    private fun onFireLongClick(fireui: FireUI): Boolean {
        return false
    }

    private fun onFireClick(fireui: FireUI) : Boolean {
        NavigationManager.goToFireDetailFragment(parentFragmentManager, fireui)
        return false
    }

    private fun updateFireList(fires: List<FireUI>) {
        CoroutineScope(Dispatchers.Main).launch {
            adapter.updateItems(fires)
        }
    }

    private fun drawMarker(latitude: Double, longitude: Double, title: String): Marker? {
        Log.i("AAAAAAAAA", "AAAAAAA")
        if (this.map != null) {
            val latLng = LatLng(latitude, longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(title)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            map!!.addMarker(markerOptions)
            Log.i("AAAAAAAAA", "$latLng")
        }
        return null
    }


    private fun onMarkerClick(marker: Marker) : Boolean {
        for (fire in adapter.getItems()) {
            if (fire.uuid == marker.title) {
                onFireClick(fire)
            }
        }
        return false
    }

    private fun drawFogosOnMap() {
        for (fire in adapter.getItems()) {
            Log.i("LAT LNG", "${fire.lat} ${fire.lng}")
            // replace to Leiria if no lat and lng
            val marker = if (fire.lat == 0.0 && fire.lng == 0.0) {
                drawMarker(39.74, -8.8, fire.uuid)
            } else {
                drawMarker(fire.lat, fire.lng, fire.uuid)
            }

            if (marker != null) {
                map?.setOnMarkerClickListener {
                    onMarkerClick(marker)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener(this)
    }
}