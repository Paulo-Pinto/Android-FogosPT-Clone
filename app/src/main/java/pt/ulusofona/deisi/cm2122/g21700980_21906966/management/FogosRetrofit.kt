package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import retrofit2.HttpException
import retrofit2.Retrofit

// API
class FogosRetrofit(retrofit: Retrofit) : Fogospt() {

    private val TAG = FogosRetrofit::class.java.simpleName
    private val service = retrofit.create(FireService::class.java)

    override fun insertFires(fires: List<FireUI>, onFinished: (List<FireUI>) -> Unit) {
        throw Exception("Not implemented on web service")
    }

    override fun deleteFire(uuid: String, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteAllFires(onFinished: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getFireList(
        onFinished: (List<FireUI>) -> Unit,
        district: String,
        radius: Int,
        coordinates: Pair<Double, Double>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fires = service.getAll()

                onFinished(fires.data.map {
                    val dist = calcDistance(coordinates.first, coordinates.second, it.lat, it.lng)
//                    Log.i("DISTANCE ", "$coordinates, ${it.lat}, ${it.lng} : dist = $dist")

                    FireUI(
                        api = true,
                        it.district,
                        it.concelho,
                        it.freguesia,
                        it.location,
                        "obs",
                        it.status,

                        it.date,

                        it.hour,
                        it.lat,
                        it.lng,
                        it.man,
                        distance = dist,

                        submitter_cc = "12345678",
                        submitter_name = "API NAME",
                        submitter_apelido = "API APELIDO",

                        sadoId = it.sadoId
                    )
                })
            } catch (ex: HttpException) {
                Log.e(TAG, ex.message())
            }
        }
    }

    private fun calcDistance(
        lat_start: Double,
        lng_start: Double,
        lat_dest: Double,
        lng_dest: Double
    ): Int {
        val theta: Double = lng_start - lng_dest

        var dist = (Math.sin(deg2rad(lat_start))
                * Math.sin(deg2rad(lat_dest))
                + (Math.cos(deg2rad(lat_start))
                * Math.cos(deg2rad(lat_dest))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515

        return dist.toInt()
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun deleteAPIFires(onFinished: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addToFireList(fire: Fire, onFinished: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getRisk(onFinished: (String) -> Unit, district: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val riskCleaned = service.getRisk(district).data.split(",")[0].split("-")[1].trim()
            Log.i("GotRisk", " $riskCleaned for $district")
            onFinished(riskCleaned)
        }
    }
}