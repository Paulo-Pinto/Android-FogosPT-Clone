package pt.ulusofona.deisi.cm2122.g21700980_21906966.extra

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
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentExtraBinding
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository

private const val TAG = "Extra Fragment TAG"

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
    private val repo = FogosRepository.getInstance()

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

        getAllOperationsWs { list ->
            println("FOOOOOOOOOOOR")
            for (l in list) {
                binding.fireId.text = l.uuid
                binding.firePlace.text = l.district + l.county
                binding.fireState.text = l.status
            }
        }

        binding.firePlace.text = "fora"

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
            binding.risk.text = repo.getRisk(repo.getRisk())

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

    private fun getAllOperationsWs(callback: (List<Fire>) -> Unit) {
        data class GetAllOperationsResponse(
            val uuid: String,
            val expression: String,
            val result: Double,
            val timestamp: Long
        )

        CoroutineScope(Dispatchers.IO).launch {
            val request: Request = Request.Builder()
                .url("https://cm-calculadora.herokuapp.com/api/operations")
                .addHeader(
                    "apikey",
                    "8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17"
                )
                .build()

            val response = OkHttpClient().newCall(request).execute().body
            if (response != null) {
                val responseObj =
                    Gson().fromJson(response.string(), Array<GetAllOperationsResponse>::class.java)
                        .toList()
                callback(responseObj.map {
                    Fire(
                        api = true,
                        uuid = it.uuid,
                        district = it.expression,
                        county = it.expression,
                        parish = it.timestamp.toString(),
                        obs = "Expression",
                        status = it.result.toString(),
                        submitter_cc = "00000000",
                        submitter_name = "00000000",
                        submitter_apelido = "00000000",
                        sadoId = "sado"
                    )
                })
            }
        }
    }

}

// API KEY calculadora : 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17