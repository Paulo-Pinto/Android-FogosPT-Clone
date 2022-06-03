package pt.ulusofona.deisi.cm2122.g21700980_21906966.fire

import java.util.*

data class Fire(
    val uuid: String = UUID.randomUUID().toString(),
    val district: String,
    val county: String,
    val parish: String,
    val obs: String,
    val state: String,
    val submitter: Person
) {

    val timestamp: Long = Date().time
    val place = "$district, $county, $parish"
    val firefighters: Int = 20

    // TODO : faltam fotos
    //val pic = ImageView(R.drawable.ic_fire_red) não dá para aceder por causa do mvvm
}