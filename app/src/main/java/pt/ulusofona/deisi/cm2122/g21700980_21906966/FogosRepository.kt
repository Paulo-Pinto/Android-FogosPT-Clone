package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.connectivity.ConnectivityUtil
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
import java.util.*
import kotlin.random.Random

class FogosRepository private constructor(
    private val context: Context,
    private val local: Fogospt, private val remote: Fogospt
) {

    private var district = "placeholder"
    private var county = "placeholder"
    private var parish = "placeholder"
    private var observations = "placeholder observations"
    private var submitter = Person("Anonymous", "99999999")
    private var status = "Por Confirmar"

    public fun setLocation(d: String, c: String, f: String): String {
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
        status = s
    }

    fun setRandomStatus() {
        status = listOf("Por confirmar", "Em curso", "Concluído")[Random.nextInt(0, 3)]
    }

    fun addFire(onSaved: () -> Unit) {

        val fire = Fire(
            district = district,
            county = county,
            parish = parish,
            obs = observations,
            status = status,
            submitter_cc = submitter.getCc(),
        )

        CoroutineScope(Dispatchers.IO).launch {
            local.addToFireList(fire)
            onSaved()
        }
    }

    fun deleteFire(uuid: String, onSuccess: () -> Unit) {
        remote.deleteFire(uuid, onSuccess)
    }

    fun getFireList(
        onFinished: (List<FireUI>) -> Unit,
        district: String = "Portugal",
        radius: Int = 999
    ) {
        if (ConnectivityUtil.isOnline(context)) {
            remote.getFireList({ fireList ->
                local.deleteAllFires {
                    local.insertFires(fireList) {
                        onFinished(fireList)
                    }
                }
            }, district, radius)
        } else {
            local.getFireList(onFinished)
        }
    }

    companion object {

        private var instance: FogosRepository? = null

        fun init(context: Context, local: Fogospt, remote: Fogospt) {
            synchronized(this) {
                if (instance == null) {
                    instance = FogosRepository(context, local, remote)
                }
            }
        }

        fun getInstance(): FogosRepository {
            return instance ?: throw IllegalStateException("Repository not initialized")
        }

    }

}