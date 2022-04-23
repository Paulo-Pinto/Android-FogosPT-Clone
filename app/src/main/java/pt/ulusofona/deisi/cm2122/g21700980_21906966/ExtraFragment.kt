package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.graphics.Color
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
    private var fire: FireUI? = null
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
        binding.risk.postDelayed(Runnable {
            // deviam ser 20 segundos mas depois não se nota
            binding.risk.postDelayed(runnable, 2500)
            val risk = risks[++ctr % risks.size]
            binding.risk.text = "Risco ${risk.first}"
            binding.risk.setTextColor(Color.parseColor(risk.second))
        }.also { runnable = it }, 2500)
    }
}