package pt.ulusofona.deisi.cm2122.g21700980_21906966.register

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.FogosViewModel
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI

class RegisterFireFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: FogosViewModel

    // TODO : risco devia ser fragment para não ser tanto hassle
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
            getString(R.string.register_fire)
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this)[FogosViewModel::class.java]
        binding = FragmentRegisterBinding.bind(view)

        val risk = risks[0]
        binding.risk.text = "Risco ${risk.first}"

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.onGetFireList { updateFireList(it) }

        // add districts to spinner
        ArrayAdapter.createFromResource(
            requireContext(), R.array.districts_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.districtSpinner?.adapter = adapter
            binding.districtSpinnerLandscape?.adapter = adapter
        }

        binding.buttonFire.setOnClickListener { onClickCreateFire() }
        binding.buttonFireInstant?.setOnClickListener { onClickCreatePremadeFire() }
    }

    override fun onResume() {
        super.onResume()

        // change risk message
        binding.risk.postDelayed(Runnable {
            // TODO : alterar para 20 segundos = 20 * 1000
            binding.risk.postDelayed(runnable, 2500)
            val risk = risks[++ctr % risks.size]
            binding.risk.text = "Risco ${risk.first}"
            binding.risk.setTextColor(Color.parseColor(risk.second))

            // pode estar depois do super.onresume()
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127,127,127)
                binding.risk.setTextColor(gray)
            }
        }.also { runnable = it }, 2500)

    }

    private fun updateAttributes(): Boolean {
        val red = R.color.red;
        val orange = R.color.orange_warning;
        val black = R.color.black;
        // location
        val orientation = resources.configuration.orientation

        val district = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.districtSpinnerLandscape?.selectedItem.toString() //  landscape
        } else {
            binding.districtSpinner?.selectedItem.toString() // portrait
        }

        if (district == "") {
            Toast.makeText(context, getString(R.string.district_unfilled), Toast.LENGTH_LONG).show()
            return false
        }

        val county: String = binding.inputCounty.text.toString()
        if (county == "") {
            Toast.makeText(context, getString(R.string.county_unfilled), Toast.LENGTH_LONG).show()
            binding.inputCounty.background.setTint(red)
            return false
        }
        binding.inputCounty.background.setTint(black)

        val parish: String = binding.inputParish.text.toString()
        if (parish == "") {
            Toast.makeText(context, getString(R.string.parish_unfilled), Toast.LENGTH_LONG).show()
            binding.inputParish.background.setTint(red)
            return false
        }
        binding.inputParish.background.setTint(black)

        viewModel.setLocation(district, county, parish)

        // person
        val name: String = binding.inputName.text.toString()
        if (name == "") {
            Toast.makeText(context, getString(R.string.name_unfilled), Toast.LENGTH_LONG).show()
            binding.inputName.background.setTint(red)
            return false
        }
        binding.inputName.background.setTint(black)

        val cc: String = binding.inputCc.text.toString()
        if (cc == "") {
            Toast.makeText(context, getString(R.string.cc_unfilled), Toast.LENGTH_LONG).show()
            binding.inputCc.background.setTint(red)
            return false
        } else if (cc.length < 8) {
            Toast.makeText(context, getString(R.string.cc_incomplete), Toast.LENGTH_LONG).show()
            binding.inputCc.background.setTint(orange)
            return false
        }
        binding.inputCc.background.setTint(black)

        viewModel.setSubmitter(Person(name, cc))

        //obs
        viewModel.setObservations((binding.inputObs.text.toString()))

        viewModel.setState("Por Confirmar")

//        Toast.makeText(context, "Dados atualizados", Toast.LENGTH_LONG).show()
        return true
    }

    private fun onClickCreateFire() {
        if (updateAttributes()) {
            viewModel.addFire {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, getString(R.string.fire_registered), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun onClickCreatePremadeFire() {
        viewModel.setLocation("Lisboa", "Lisboa", "Alvalade")
        viewModel.setSubmitter(Person("Vincent Aboubakar", "12345678"))
        viewModel.setObservations("Fogo pré-feito")
        viewModel.setRandomState()

        viewModel.addFire {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, getString(R.string.fire_registered), Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun updateFireList(fires: List<FireUI>) {

        fires.map {
            FireUI(
                it.uuid,
                it.timestamp,
                it.district,
                it.county,
                it.parish,
                it.obs,
                it.status,
                it.submitter.getName(),
                it.submitter.getCc(),
            )
        }
    }

}