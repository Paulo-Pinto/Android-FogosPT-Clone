package pt.ulusofona.deisi.cm2122.g21700980_21906966.fire

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "fire")
data class Fire(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val district: String,
    val county: String,
    val parish: String,
    val obs: String,
    val status: String,
    val submitter_cc: String,
    val timestamp: Long = Date().time,

    val place: String = "",
    val firefighters: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val date : String = "1970",
    val hour : String = "12:00",
) {

    // TODO : faltam fotos
    //val pic = ImageView(R.drawable.ic_fire_red) não dá para aceder por causa do mvvm
}

@Parcelize
data class FireUI(
    val uuid: String,
    val timestamp: Long?,
    val district: String,
    val county: String,
    val parish: String,
    val obs: String,
    val status: String,
    val submitterName: String,
    val submitterCc: String,

    val place: String = "",
    val firefighters: Int = 0,
//    val last_updated: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val date : String = "1970",
    val hour : String = "12:00",
) : Parcelable {

    @IgnoredOnParcel
    val submitter: Person = Person(submitterName, submitterCc)
}
