package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire

// Este array deverá conter todas as entidades do modelo de dados
@Database(entities = [Fire::class], version = 5)
abstract class FireDatabase : RoomDatabase() {

    abstract fun fogosDao(): FogosDao

    companion object {

        private var instance: FireDatabase? = null

        fun getInstance(applicationContext: Context): FireDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        applicationContext,
                        FireDatabase::class.java,
                        "fire_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance as FireDatabase
            }
        }
    }

}