package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.content.Context
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.chromium.net.CronetEngine
import org.chromium.net.UrlRequest
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentExtraBinding
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ExtraFragment : Fragment() {

    private var runnable: Runnable? = null
    private lateinit var binding: FragmentExtraBinding
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
//API things
//        val myBuilder = CronetEngine.Builder(context)
//        val cronetEngine: CronetEngine = myBuilder.build()
//        val executor: Executor = Executors.newSingleThreadExecutor()
//        val requestBuilder = cronetEngine.newUrlRequestBuilder(
//            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relativehumidity_2m,windspeed_10m",
//            MyUrlRequestCallback(),
//            executor
//        )
//
//        val request: UrlRequest = requestBuilder.build()
//        binding.submitter.text=request.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "Fogo dsdsffdsfdsfdsfd"

        val view = inflater.inflate(R.layout.fragment_extra, container, false)
        binding = FragmentExtraBinding.bind(view)

        val risk = risks[0]
        binding.risk.text = "Risco ${risk.first}"

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.submitter.text = "AAAA"

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

            // pode estar depois do super.onresume()
            val bm = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batLevel <= 20) {
                val gray = Color.rgb(127,127,127)
                binding.risk.setTextColor(gray)
            }
        }.also { runnable = it }, 20000)
    }
}