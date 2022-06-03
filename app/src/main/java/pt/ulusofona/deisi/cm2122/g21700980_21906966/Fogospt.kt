package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
import kotlin.random.Random

object Fogospt {

    private val fireList = mutableListOf<Fire>()

    private var district = "placeholder"
    private var county = "placeholder"
    private var parish = "placeholder"
    private var observations = "placeholder observations"
    private var submitter = Person("Anonymous", "99999999")
    private var state = "Por Confirmar"

    fun setLocation(d: String, c: String, f: String): String {
        district = d
        county = c
        parish = f
        return "$d, $c, $f"
    }

    fun setSubmitter(person: Person): String {
        submitter = person
        return submitter.toString()
    }

    fun setObservations(obs: String) {
        observations = obs
    }

    fun setState(s: String) {
        state = s
    }

    fun setRandomState() {
        state = listOf("Por confirmar", "Em curso", "Concluído")[Random.nextInt(0, 3)]
    }

    // NÃO APAGAR
    fun deleteFire(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fire = fireList.find { it.uuid == uuid }
            fireList.remove(fire)
            onSuccess()
        }
    }

    // Gerar Fogo
    fun addFire(onSaved: () -> Unit) {

        val fire = Fire(
            district = district,
            county = county,
            parish = parish,
            obs = observations,
            state = state,
            submitter = submitter,
        )

        CoroutineScope(Dispatchers.IO).launch {
            addToFireList(fire)
            onSaved()
        }
    }

    fun getFireList(
        onFinished: (List<Fire>) -> Unit,
        district: String = "Portugal",
        radius: Int = 0,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("asdkopasdko", "Aboubakar $district $radius")
            Log.i("asdkopasdko", "$fireList")
            // normal
            if (district == "Portugal" && radius == 0) {
                Log.i("asdkopasdko", "ENTROU!!")
                onFinished(fireList.toList())
            } else {

            val filteredList = mutableListOf<Fire>()

            for (fire in fireList) {
                // calc distance
                // && dist < radius
                if (fire.district == district) {
                    filteredList.add(fire)
                }
            }

            onFinished(filteredList.toList())
        }
        }
    }

    private fun addToFireList(fire: Fire) {
//        Thread.sleep(10 * 1000)
        fireList.add(fire)
    }

}
