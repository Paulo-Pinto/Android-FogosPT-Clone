package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI

abstract class Fogospt {

    abstract fun deleteFire(uuid: String, onSuccess: () -> Unit)
    abstract fun deleteAllFires(onFinished: () -> Unit)
    abstract fun insertFires(fires: List<FireUI>, onFinished: (List<FireUI>) -> Unit)
    abstract fun getFireList(
        onFinished: (List<FireUI>) -> Unit,
        district: String = "Portugal",
        radius: Int = 999
    )

    abstract fun deleteAPIFires(onFinished: () -> Unit)

    abstract fun addToFireList(fire: Fire, onSaved: () -> Unit)
    abstract fun getRisk(function: (String) -> Unit, district: String = "Portugal")
}
