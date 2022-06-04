package pt.ulusofona.deisi.cm2122.g21700980_21906966.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.util.Log

object ConnectivityUtil {

    private val TAG = ConnectivityUtil::class.java.simpleName

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(TRANSPORT_CELLULAR)) {
                Log.i(TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(TRANSPORT_WIFI)) {
                Log.i(TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(TRANSPORT_ETHERNET)) {
                Log.i(TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

}