package pt.ulusofona.deisi.cm2122.g21700980_21906966

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI

class FogosRoom(private val dao: FogosDao) : Fogospt() {

    override fun insertFires(fires: List<FireUI>, onFinished: (List<FireUI>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val history = fires.map {
                Fire(
                    it.uuid,
                    it.district,
                    it.county,
                    it.parish,
                    it.obs,
                    it.status,
                    it.submitter.getCc(),
                ) }
            dao.insertAll(history)
            onFinished(fires)
        }
    }

    override fun deleteFire(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = dao.delete(uuid)
            // se == 1 eliminou o registo, se for 0 não encontrou o registo a eliminar
            if(result == 1) onSuccess()
        }
    }

    override fun deleteAllFires(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
            onFinished()
        }
    }

    override fun getFireList(onFinished: (List<FireUI>) -> Unit, district: String, radius: Int) {
        CoroutineScope(Dispatchers.IO).launch {
//             TODO : if district == portugal get all else get district
            val fires = dao.getAll()
            onFinished(fires.map {
                FireUI(
                    it.uuid,
                    it.timestamp,
                    it.district,
                    it.county,
                    it.parish,
                    it.obs,
                    it.status,
                    it.submitter_cc,
                    it.submitter_cc
                )
            })
        }
    }



}