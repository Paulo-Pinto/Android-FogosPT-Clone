package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentFireDetailBinding
import java.text.SimpleDateFormat

private const val ARG_FIRE = "ARG_FIRE"

class FireDetailFragment : Fragment() {

    private var runnable: Runnable? = null
    private var fire: FireUI? = null
    private lateinit var binding: FragmentFireDetailBinding
    private val risks = listOf(
        Pair("Reduzido", "#4d87e3"),
        Pair("Moderado", "#46a112"),
        Pair("Elevado", "#f7dd72"),
        Pair("Muito Elevado", "#e34814"),
        Pair("Máximo", "#da291c"),
    )
    private var ctr = 0

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
            binding.fireState.text = it.state
            binding.firePlace.text = it.place
            binding.fireTimestamp.text = sdf.format(it.timestamp)
            binding.fireObservations.text = it.obs
            binding.fireResources.text = it.firefighters.toString() + " bombeiros"
            binding.submitter.text = it.submitter.toString()
        }
        val risk = risks[0]
        binding.risk.text = "Risco ${risk.first}"
        binding.risk.setTextColor(Color.parseColor(risk.second))
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