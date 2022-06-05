package pt.ulusofona.deisi.cm2122.g21700980_21906966.dashboard

import android.content.Context
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentDashboardBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository

class DashboardFragment : Fragment() {

    private val repo = FogosRepository.getInstance()
    private lateinit var binding: FragmentDashboardBinding
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.dashboard)

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        binding = FragmentDashboardBinding.bind(view)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // change risk message
        updateRisk()
    }

    // RISK
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

}