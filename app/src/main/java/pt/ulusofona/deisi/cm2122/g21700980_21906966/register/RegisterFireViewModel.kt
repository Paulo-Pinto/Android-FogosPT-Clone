package pt.ulusofona.deisi.cm2122.g21700980_21906966.register

import androidx.lifecycle.ViewModel
import pt.ulusofona.deisi.cm2122.g21700980_21906966.Fogospt
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Fire

class RegisterFireViewModel : ViewModel() {

    private val model = Fogospt

    fun setLocation(district: String, county: String, parish: String): String =
        Fogospt.setLocation(district, county, parish)

    fun setSubmitter(person: Person): String = Fogospt.setSubmitter(person)

    fun setObservations(obs: String) = Fogospt.setObservations(obs)

    fun setState(s: String) = Fogospt.setState(s)

    fun setRandomState() = Fogospt.setRandomState()

    fun onGetFireList(onFinished: (List<Fire>) -> Unit) {
        Fogospt.getFireList(onFinished)
    }

    fun addFire(onSaved: () -> Unit) {
        Fogospt.addFire(onSaved)
    }
}