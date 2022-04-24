package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentDashboardBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentMapBinding

class MapFragment: Fragment()  {

    private lateinit var binding: FragmentMapBinding

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

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        binding = FragmentMapBinding.bind(view)
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
        }.also { runnable = it }, 20000)
    }

}