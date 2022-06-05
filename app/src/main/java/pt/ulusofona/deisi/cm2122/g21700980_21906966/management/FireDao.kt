package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire

@Dao
interface FogosDao {

    @Insert
    suspend fun addToFireList(fire: Fire)

    @Insert
    suspend fun insertAll(fires: List<Fire>)

    @Query("SELECT * FROM fire WHERE distance < :r ORDER BY timestamp ASC")
    suspend fun getAll(r: Int): List<Fire>

    @Query("SELECT * FROM fire WHERE district = :d and distance < :r  ORDER BY timestamp ASC")
    suspend fun getAllByDistrict(d: String, r: Int): List<Fire>

    @Query("SELECT * FROM fire WHERE distance < :r ORDER BY timestamp ASC")
    suspend fun getAllByRadius(r: Int): List<Fire>

//    @Query("SELECT * FROM fire WHERE distance < r ORDER BY timestamp ASC")
//    suspend fun getAllWithRadius(r : Int): List<FireRoom>

    @Query("SELECT * FROM fire WHERE uuid = :uuid")
    suspend fun getById(uuid: String): Fire

    @Query("DELETE FROM fire WHERE uuid = :uuid")
    suspend fun delete(uuid: String): Int

    @Query("DELETE FROM fire")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM fire WHERE api = 1")
    suspend fun deleteAllFromAPI(): Int
}