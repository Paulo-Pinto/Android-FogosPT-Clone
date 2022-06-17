package pt.ulusofona.deisi.cm2122.g21700980_21906966.fire

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentFireDetailBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository
import java.text.SimpleDateFormat


private const val ARG_FIRE = "ARG_FIRE"

class FireDetailFragment : Fragment() {

    private var runnable: Runnable? = null
    private var fire: FireUI? = null
    private lateinit var binding: FragmentFireDetailBinding

    private val repo = FogosRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { fire = it.getParcelable(ARG_FIRE) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fire?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Fogo in ${it.parish}, ${it.county}"
        }
        val view = inflater.inflate(R.layout.fragment_fire_detail, container, false)
        binding = FragmentFireDetailBinding.bind(view)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss")

        fire?.let {
            binding.fireUuid.text = it.uuid
            binding.fireState.text = it.status
            binding.firePlace.text = it.location
            binding.fireTimestamp.text = sdf.format(it.timestamp)
            binding.fireObservations.text = "${it.lat} , ${it.lng}"
            binding.fireResources.text = it.man.toString() + " bombeiros"
            binding.submitter.text = "${it.submitter_name} ; ${it.submitter_apelido} [${it.submitter_cc}]"
        }
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
            // mudar cor - TODO : crashes dunno why
//            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
//            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
//            if (batLevel <= 20) {
//                val gray = Color.rgb(127, 127, 127)
//                binding.risk.setTextColor(gray)
//            } else {
                var cor = when (binding.risk.text) {
                    "Reduzido" -> "#4d87e3"
                    "Moderado" -> "#46a112"
                    "Elevado" -> "#f7dd72"
                    "Muito Elevado" -> "#e34814"
                    "Máximo" -> "#da291c"
                    else -> "#46a112"
                }

                binding.risk.setTextColor(Color.parseColor(cor))
//            }

        }.also { runnable = it }, 0)
    }


    companion object {
        @JvmStatic
        fun newInstance(fire: FireUI) =
            FireDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FIRE, fire)
                }
            }
    }
}