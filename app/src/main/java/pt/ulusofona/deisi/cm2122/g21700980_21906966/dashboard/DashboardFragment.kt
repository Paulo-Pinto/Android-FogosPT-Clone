package pt.ulusofona.deisi.cm2122.g21700980_21906966.dashboard

import android.content.Context
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

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
            getString(R.string.dashboard)

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        binding = FragmentDashboardBinding.bind(view)

        val risk = risks[0]
        binding.risk.text = "Risco ${risk.first}"

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
            // TODO : pode haver outro post delayed mais rápido, só para trocar a cor
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127, 127, 127)
                binding.risk.setTextColor(gray)
            }
        }.also { runnable = it }, 20000)
    }

}