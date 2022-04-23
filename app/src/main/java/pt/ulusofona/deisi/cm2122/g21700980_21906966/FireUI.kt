package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class FireUI(
    val uuid: String,
    val timestamp: Long,
    val district: String,
    val county: String,
    val parish: String,
    val obs: String,
    val submitterName: String,
    val submitterCc: String,
    val state: String,
) : Parcelable {
    @IgnoredOnParcel
    val place = "$district, $county, $parish"

    @IgnoredOnParcel
    val firefighters: Int = 20

    @IgnoredOnParcel
    val submitter: Person = Person(submitterName, submitterCc)
}
