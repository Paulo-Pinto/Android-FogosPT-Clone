package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import retrofit2.*
import java.lang.Exception

class FogosRetrofit(retrofit: Retrofit) : Fogospt() {

    private val TAG = FogosRetrofit::class.java.simpleName
    private val service = retrofit.create(FireService::class.java)
    private var usedIds = mutableListOf<String>()

    override fun insertFires(fires: List<FireUI>, onFinished: (List<FireUI>) -> Unit) {
        throw Exception("Not implemented on web service")
    }

    override fun deleteFire(uuid: String, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteAllFires(onFinished: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getFireList(onFinished: (List<FireUI>) -> Unit, district: String, radius: Int) {
        Log.i("retrofit", "GetFireList")

        CoroutineScope(Dispatchers.IO).launch {
            try {
//                if(district != "Portugal") {
//                    val fires = service.getAllByDistrict()
//                }
                val fires = service.getAll()

                onFinished(fires.data.map {
                    FireUI(
                        api = true,
                        it.district,
                        it.concelho,
                        it.freguesia,
                        it.location,
                        "obs",
                        it.status,
                        "12345678",
                        0L,
                        it.date,
                        it.hour,
                        it.lat,
                        it.lng,
                        it.man,
                    )
                })
            } catch (ex: HttpException) {
                Log.e(TAG, ex.message())
            }
        }
    }

    override fun deleteAPIFires(onFinished: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addToFireList(fire: Fire, onFinished: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getRisk(onFinished: (String) -> Unit, district: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val risk = service.getRisk(district).risk.split(",")[0].split("-")[1].trim()
            onFinished(risk)
        }
    }
}