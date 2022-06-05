package pt.ulusofona.deisi.cm2122.g21700980_21906966.map

import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentMapBinding
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FireService
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository

class MapFragment : Fragment(), OnLocationChangedListener {

    private lateinit var binding: FragmentMapBinding
    private val repo = FogosRepository.getInstance()
    // mapa
    private lateinit var geocoder: Geocoder
    private var district = "Portugal"
    private var map: GoogleMap? = null

    // risk
    private var runnable: Runnable? = null
    private val risks = listOf(
        Pair("Reduzido", "#4d87e3"),
        Pair("Moderado", "#46a112"),
        Pair("Elevado", "#f7dd72"),
        Pair("Muito Elevado", "#e34814"),
        Pair("Máximo", "#da291c"),
    )
    private var ctr = 0

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

        val risk = risks[0]
        binding.risk.text = "Risco ${risk.first}"

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()

        // change risk message
        binding.risk.postDelayed(Runnable {
            binding.risk.postDelayed(runnable, 20000)
            val risk = risks[++ctr % risks.size]
//            binding.risk.text = "Risco ${risk.first}"
            binding.risk.setTextColor(Color.parseColor(risk.second))

            binding.risk.text = repo.getRisk({ }, district).toString()

            // pode estar depois do super.onresume()
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127, 127, 127)
                binding.risk.setTextColor(gray)
            }
        }.also { runnable = it }, 20000)
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        placeCamera(latitude, longitude)
        placeCityName(latitude, longitude)
    }

    private fun placeCamera(latitude: Double, longitude: Double) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(12f)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun placeCityName(latitude: Double, longitude: Double) : String {
        val addresses = geocoder.getFromLocation(latitude, longitude, 5)
        val location = addresses.first { it.locality != null && it.locality.isNotEmpty() }
        binding.tvCityName.text = location.locality
        return location.locality
    }

    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener(this)
    }
}