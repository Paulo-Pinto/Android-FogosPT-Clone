package pt.ulusofona.deisi.cm2122.g21700980_21906966.connectivity

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    fun getInstance(path: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(path)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}