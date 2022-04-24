package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.FragmentFireListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FireListFragment : Fragment() {

    private val model = Fogospt
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
        binding.risk.setTextColor(Color.parseColor(risk.second))

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

    override fun onStart() {
        super.onStart()
        binding.firelist.layoutManager = LinearLayoutManager(context)
        binding.firelist.adapter = adapter
        model.getFireList { updateFireList(it) }
    }

    private fun onFireClick(fireui: FireUI) {
        NavigationManager.goToFireDetailFragment(parentFragmentManager, fireui)
    }

    private fun onFireLongClick(fireui: FireUI): Boolean {
        Toast.makeText(context, getString(R.string.delete), Toast.LENGTH_SHORT).show()
        model.deleteFire(fireui.uuid) { model.getFireList { updateFireList(it) } }
        return false
    }

    private fun updateFireList(fires: List<Fire>) {
        val fireList = fires.map {
            FireUI(
                it.uuid,
                it.timestamp,
                it.district,
                it.county,
                it.parish,
                it.obs,
                it.submitter.getName(),
                it.submitter.getCc(),
                it.state
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