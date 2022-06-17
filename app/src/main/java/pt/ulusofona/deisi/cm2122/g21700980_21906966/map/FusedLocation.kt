package pt.ulusofona.deisi.cm2122.g21700980_21906966.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
class FusedLocation private constructor(context: Context) : LocationCallback() {
    private lateinit var last_coords: Pair<Double, Double>
    private val TAG = FusedLocation::class.java.simpleName

    // Intervalos de tempo em que a localização é verificada, 5 segundos
    private val TIME_BETWEEN_UPDATES = 5 * 1000L

    // Configurar a precisão e os tempos entre atualizações da localização
    private var locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = TIME_BETWEEN_UPDATES
    }

    // Este atributo será utilizado para acedermos à API da Fused Location
    private var client = FusedLocationProviderClient(context)

    init {
        // Instanciar o objeto que permite definir as configurações
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        // Aplicar as configurações ao serviço de localização
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)

        client.requestLocationUpdates(locationRequest, this, Looper.getMainLooper())
    }

    override fun onLocationResult(locationResult: LocationResult) {
//        Log.i(TAG, locationResult.lastLocation.toString())
        last_coords =
            Pair(locationResult.lastLocation!!.latitude, locationResult.lastLocation!!.longitude)
        notifyListener(locationResult)
    }

    fun getLastCoords(): Pair<Double,Double> {
        return last_coords
    }

    companion object {
        // Se quisermos ter vários listeners isto tem de ser uma lista
        private val listeners = mutableListOf<OnLocationChangedListener>()
        private var instance: FusedLocation? = null

        fun registerListener(listener: OnLocationChangedListener) {
            listeners.add(listener)
        }

        fun unregisterListener(listener: OnLocationChangedListener) {
            listeners.remove(listener)
        }

        // Se tivermos vários listeners, temos de os notificar com um forEach
        fun notifyListener(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            listeners.forEach { it.onLocationChanged(location!!.latitude, location.longitude) }
        }

        // Só teremos uma instância em execução
        fun start(context: Context) {
            instance = if (instance == null) FusedLocation(context) else instance
        }
    }
}

interface OnLocationChangedListener {


    fun onLocationChanged(latitude: Double, longitude: Double)

}