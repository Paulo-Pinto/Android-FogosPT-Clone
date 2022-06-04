package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
import kotlin.random.Random

abstract class Fogospt {

    private val fireList = mutableListOf<Fire>()

    // TODO : onFinished ou onSuccess ???
    abstract fun deleteFire(uuid: String, onSuccess: () -> Unit)
    abstract fun deleteAllFires(onFinished: () -> Unit)
    abstract fun insertFires(fires: List<FireUI>, onFinished: (List<FireUI>) -> Unit)
    abstract fun getFireList(
        onFinished: (List<FireUI>) -> Unit,
        district: String = "Portugal",
        radius: Int = 999
    )

    fun addToFireList(fire: Fire) {
//        Thread.sleep(10 * 1000)
        fireList.add(fire)
    }

//    fun getFireList(
//        onFinished: (List<FireRoom>) -> Unit,
//        district: String = "Portugal",
//        radius: Int = 0,
//    ) {
//        CoroutineScope(Dispatchers.IO).launch {
//            // normal
//            if (district == "Portugal" && radius == 0) {
//                Log.i("asdkopasdko", "ENTROU!!")
//                onFinished(fireList.toList())
//            } else {
//
//                val filteredList = mutableListOf<FireRoom>()
//
//                for (fire in fireList) {
//                    // calc distance
//                    // && dist < radius
//                    if (fire.district == district) {
//                        filteredList.add(fire)
//                    }
//                }
//
//                onFinished(filteredList.toList())
//            }
//        }
//    }


}
