package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.connectivity.ConnectivityUtil
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
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

    private var api_read = false
    private var risk = "Risco Normal"

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

    fun addFire(onFinished: () -> Unit) {

        val fire = Fire(
            api = false,
            district = district,
            county = county,
            parish = parish,
            location = "$county, $parish",

            obs = observations,
            status = status,

            submitter_cc = submitter.getCc(),
        )

        CoroutineScope(Dispatchers.IO).launch {
            local.addToFireList(fire) { onFinished() }
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
        local.getFireList(onFinished)

        if (ConnectivityUtil.isOnline(context)) {

            local.deleteAPIFires {}

            remote.getFireList({ fireList ->
                local.insertFires(fireList) {
                    local.getFireList(onFinished)
//                        onFinished(fireList)
                }
            }, district, radius)

        } else {
            local.getFireList(onFinished)
        }
    }


    fun getRisk(
        onFinished: (String) -> Unit,
        district: String = "Portugal"
    ) {
        if (ConnectivityUtil.isOnline(context)) {
            remote.getRisk(onFinished, district)
        } else {
            local.getRisk(onFinished, district)
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