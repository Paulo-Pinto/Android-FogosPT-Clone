package pt.ulusofona.deisi.cm2122.g21700980_21906966.list

import android.content.Context
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
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
import pt.ulusofona.deisi.cm2122.g21700980_21906966.*
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentFireListBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository

class FireListFragment : Fragment() {

    private val model = FogosRepository.getInstance()
    private var adapter =
        FireListAdapter(onClick = ::onFireClick, onLongClick = ::onFireLongClick)
    private lateinit var binding: FragmentFireListBinding

    private var runnable: Runnable? = null
    private var ctr = 0
    private val risks = listOf(
        Pair("Reduzido", "#4d87e3"),
        Pair("Moderado", "#46a112"),
        Pair("Elevado", "#f7dd72"),
        Pair("Muito Elevado", "#e34814"),
        Pair("Máximo", "#da291c"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.fire_list)
        val view = inflater.inflate(R.layout.fragment_fire_list, container, false)
        binding = FragmentFireListBinding.bind(view)

        val risk = risks[0]
        binding.risk.text = "Risco ${risk.first}"

        binding.districtSpinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // filter district
                model.getFireList(
                    {
                        updateFireList(
                            it
                        )
                    }, district = parent.selectedItem.toString(),
                    radius = binding.radiusSlider.value.toInt()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // change risk message
        binding.risk.postDelayed(Runnable {
            binding.risk.postDelayed(runnable, 20000)
            val risk = risks[++ctr % risks.size]
            binding.risk.text = "Risco ${risk.first}"
            binding.risk.setTextColor(Color.parseColor(risk.second))

            // pode estar depois do super.onresume()
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127, 127, 127)
                binding.risk.setTextColor(gray)
            }
        }.also { runnable = it }, 0)
    }

    override fun onStart() {
        super.onStart()

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
        }

        binding.firelist.layoutManager = LinearLayoutManager(context)
        binding.firelist.adapter = adapter
//        model.getFireListFromLocal({ updateFireList(it) })
        model.getFireList({ updateFireList(it) }, binding.districtSpinner.selectedItem.toString())
    }

    private fun onFireClick(fireui: FireUI) {
        NavigationManager.goToFireDetailFragment(parentFragmentManager, fireui)
    }

    private fun onFireLongClick(fireui: FireUI): Boolean {
        Toast.makeText(context, getString(R.string.delete), Toast.LENGTH_SHORT).show()
        model.deleteFire(fireui.uuid) { model.getFireList(
            { updateFireList(it) },
            binding.districtSpinner.selectedItem.toString()
        )
        }
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

                it.submitter_cc,

                it.timestamp,
                it.date,
                it.hour,

                it.lat,
                it.lng,
                it.man
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
}