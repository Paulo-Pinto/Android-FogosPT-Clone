package pt.ulusofona.deisi.cm2122.g21700980_21906966.list

import android.content.Context
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.NavigationManager
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentFireListBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository
import pt.ulusofona.deisi.cm2122.g21700980_21906966.map.FusedLocation
import pt.ulusofona.deisi.cm2122.g21700980_21906966.map.OnLocationChangedListener

class FireListFragment : Fragment(), OnLocationChangedListener {

    private val repo = FogosRepository.getInstance()
    private var adapter =
        FireListAdapter(onClick = ::onFireClick, onLongClick = ::onFireLongClick)
    private lateinit var binding: FragmentFireListBinding

    private var runnable: Runnable? = null
    private var spinnerCtr = 0

    // FusedLocation não atualiza rápido o suf
    private var lat: Double = 38.7
    private var lng: Double = -9.7

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FusedLocation.registerListener(this)

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.fire_list)
        val view = inflater.inflate(R.layout.fragment_fire_list, container, false)
        binding = FragmentFireListBinding.bind(view)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateRisk()
    }

    // change risk message
    private fun updateRisk() {
        // change risk message
        binding.risk.postDelayed(Runnable {
            binding.risk.postDelayed(runnable, 5000)
            binding.risk.text = repo.getRisk()

            Log.i("RISCO", binding.risk.text.toString())
            // mudar cor
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127, 127, 127)
                binding.risk.setTextColor(gray)
            } else {
                var cor = when (binding.risk.text) {
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

    override fun onStart() {
        super.onStart()

        // filter district
        binding.districtSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (spinnerCtr++ > 0) {
                    Log.i("FireList", "Chose new district, $position")
                    setFires()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // add districts to spinner
        ArrayAdapter.createFromResource(
            requireContext(), R.array.districts_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.districtSpinner.adapter = adapter
        }

        binding.radiusSlider.addOnChangeListener { _, value, _ ->
            // Responds to when slider's value is changed
            if (value.toInt() == 0) {
                binding.radiusText.text = getString(R.string.noRadius)
            } else {
                binding.radiusText.text = value.toInt().toString() + " Km"
            }
            setFires()
        }

        binding.firelist.layoutManager = LinearLayoutManager(context)
        binding.firelist.adapter = adapter

        setFires()
    }

    fun setFires() {
        repo.getFireList(
            { updateFireList(it) },
            district = binding.districtSpinner.selectedItem.toString(),
            if(binding.radiusSlider.value.toInt() != 0) binding.radiusSlider.value.toInt() else 999,
            coordinates = Pair(lat, lng)
        )
    }

    private fun onFireClick(fireui: FireUI) {
        NavigationManager.goToFireDetailFragment(parentFragmentManager, fireui)
    }

    private fun onFireLongClick(fireui: FireUI): Boolean {
        Toast.makeText(context, getString(R.string.delete), Toast.LENGTH_SHORT).show()
        repo.deleteFire(fireui.uuid) {}
        return false
    }

    private fun updateFireList(fires: List<FireUI>) {
        val fireList = fires.map {
            FireUI(
                it.api,
                it.district,
                it.county,
                it.parish,
                it.location,

                it.obs,
                it.status,

                it.date,
                it.hour,
                it.lat,

                it.lng,
                it.man,
                it.timestamp,
                it.distance,

                it.submitter_cc,
                it.submitter_name,
                it.submitter_apelido,

                it.sadoId,
                )
        }
        CoroutineScope(Dispatchers.Main).launch {
            showFireList(fireList.isNotEmpty())
            adapter.updateItems(fireList)
        }
    }

    private fun showFireList(show: Boolean) {
        if (show) {
            binding.firelist.visibility = View.VISIBLE
            binding.noFiresAvailable.visibility = View.GONE
        } else {
            binding.firelist.visibility = View.GONE
            binding.noFiresAvailable.visibility = View.VISIBLE
        }
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        lat = latitude
        lng = longitude
    }

    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener(this)
    }

}