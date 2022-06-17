package pt.ulusofona.deisi.cm2122.g21700980_21906966.fire

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "fire")
data class Fire(
    val api: Boolean,

    val district: String,
    val county: String,
    val parish: String,
    val location: String = "",

    val obs: String,
    val status: String,

    val date: String = "1970",
    val hour: String = "12:00",

    val lat: Double = 39.74,
    val lng: Double = 8.80,

    val man: Int = 0,

    val timestamp: Long = Date().time,
    val distance: Int = 0,

    val submitter_cc: String,
    val submitter_name: String,
    val submitter_apelido: String,

    val sadoId : String,

    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
) {

    // TODO : faltam fotos
    //val pic = ImageView(R.drawable.ic_fire_red) não dá para aceder por causa do mvvm
}

@Parcelize
data class FireUI(
    val api: Boolean,

    val district: String,
    val county: String,
    val parish: String,
    val location: String = "",

    val obs: String,
    val status: String,

    val date: String = "1970",
    val hour: String = "12:00",

    val lat: Double = 39.74,
    val lng: Double = 8.80,
    val man: Int = 0,

    val timestamp: Long = Date().time,
    val distance: Int = 0,

    val submitter_cc: String,
    val submitter_name: String,
    val submitter_apelido: String,

    val sadoId : String,

    val uuid: String = UUID.randomUUID().toString(),
) : Parcelable {

    @IgnoredOnParcel
    val submitter: Person = Person(submitter_cc, submitter_name, submitter_apelido)
}
