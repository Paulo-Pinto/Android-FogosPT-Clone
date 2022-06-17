package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import kotlin.random.Random

// LOCAL
class FogosRoom(private val dao: FogosDao) : Fogospt() {

    override fun addToFireList(fire: Fire, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addToFireList(fire)
            onFinished()
        }
    }

    override fun insertFires(fires: List<FireUI>, onFinished: (List<FireUI>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val history = fires.map {
                Fire(
                    api = false,

                    it.district,
                    it.county,
                    it.parish,
                    it.location,

                    it.obs,
                    it.status,

                    it.submitter_cc,

                    it.date,
                    it.hour,

                    if (it.lat != 0.0) it.lat else (37.3 + Random.nextDouble() * (41.5 - 37.3)),
                    if (it.lng != 0.0) it.lng else (-8.37 + Random.nextDouble() * (-7.43 + 8.37)),

                    it.man,
                    it.timestamp,
                    it.distance
                )
            }
            dao.insertAll(history)
            onFinished(fires)
        }
    }

    override fun deleteFire(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = dao.delete(uuid)
            // se == 1 eliminou o registo, se for 0 não encontrou o registo a eliminar
            if (result == 1) onSuccess()
        }
    }

    override fun deleteAllFires(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
            onFinished()
        }
    }

    override fun deleteAPIFires(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllFromAPI()
            onFinished()
        }
    }

    override fun getFireList(
        onFinished: (List<FireUI>) -> Unit,
        district: String,
        radius: Int,
        coordinates: Pair<Double, Double>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val fires: List<Fire> = if (district == "Portugal") {
                dao.getAll(radius) // país inteiro
            } else {
                dao.getAllByDistrict(district, radius) // distrito
            }

            // remove duplicates
            var locations = mutableListOf<String>()
            var new_fires = mutableListOf<Fire>()

            for(fire in fires){
                if(!locations.contains(fire.location)){
                    locations.add(fire.location)
                    new_fires.add(fire)
                }
            }

            onFinished(new_fires.map {
                Log.i("ROOM API ", " $it + ${it.api}")
                FireUI(
                    it.api,

                    it.district,
                    it.county,
                    it.parish,
                    it.location,

                    it.obs,
                    it.status,

                    it.submitter_cc,

                    it.date,
                    it.hour,

                    if (it.lat != 0.0) it.lat else (37.3 + Random.nextDouble() * (41.5 - 37.3)),
                    if (it.lng != 0.0) it.lng else (-8.37 + Random.nextDouble() * (-7.43 + 8.37)),

                    it.man,
                    it.timestamp,
                    it.distance
                )
            })
        }
    }

    override fun getRisk(onFinished: (String) -> Unit, district: String) {
        onFinished("Desconhecido")
    }


}