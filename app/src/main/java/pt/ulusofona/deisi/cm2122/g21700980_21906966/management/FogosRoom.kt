package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI

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
                    it.api,

                    it.district,
                    it.county,
                    it.parish,
                    it.location,

                    it.obs,
                    it.status,

                    it.submitter_cc,

                    it.timestamp,
                    it.date,
                    it.hour,

                    it.lat,
                    it.lng,
                    it.man
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

    override fun deleteAPIFires(onFinished: () -> Unit)
    {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllFromAPI()
            onFinished()
        }
    }

    override fun getFireList(onFinished: (List<FireUI>) -> Unit, district: String, radius: Int) {
        CoroutineScope(Dispatchers.IO).launch {
//             TODO : if district == portugal get all else get district
            val fires = dao.getAll()
            onFinished(fires.map {
                FireUI(
                    it.api,

                    it.district,
                    it.county,
                    it.parish,
                    it.location,

                    it.obs,
                    it.status,

                    it.submitter_cc,

                    it.timestamp,
                    it.date,
                    it.hour,

                    it.lat,
                    it.lng,
                    it.man
                )
            })
        }
    }

    override fun getRisk(onFinished: (String) -> Unit, district: String) {
        onFinished("Desconhecido")
    }


}