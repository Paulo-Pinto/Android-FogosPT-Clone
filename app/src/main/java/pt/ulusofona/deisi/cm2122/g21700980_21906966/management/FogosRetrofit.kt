package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
//                    val fires = service.getAll()
//                }
                val fires = service.getAll()

                onFinished(fires.data.map {
                    if (usedIds.contains(it.id)){
                        FireUI(
                            it.id + 1 ,
                            it.created["sec"],
                            it.district,
                            it.concelho,
                            it.freguesia,
                            "observações",
                            it.status,
                            "António Silva",
                            "12345678",
                            it.location,
                            it.man,
                            it.lat,
                            it.lng,
                            it.date,
                            it.hour
                            )
                    } else {
                        usedIds.add(it.id)
                        FireUI(
                            it.id,
                            12332,
                            "district",
                            "c",
                            "f",
                            "o",
                            "name",
                            "12345678",
                            "estado",
                        )
                    }
                })
            } catch (ex: HttpException) {
                Log.e(TAG, ex.message())
            }
        }
    }

}