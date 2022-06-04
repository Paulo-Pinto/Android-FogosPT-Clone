package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import androidx.lifecycle.ViewModel
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.Person
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI

class FogosViewModel : ViewModel() {

    private val model = FogosRepository.getInstance()

    fun setLocation(district: String, county: String, parish: String): String =
        model.setLocation(district, county, parish)

    fun setSubmitter(person: Person): String = model.setSubmitter(person)

    fun setObservations(obs: String) = model.setObservations(obs)

    fun setState(s: String) = model.setState(s)

    fun setRandomState() = model.setRandomStatus()

    fun onGetFireList(onFinished: (List<FireUI>) -> Unit) {
        model.getFireList(onFinished)
    }

    fun addFire(onSaved: () -> Unit) {
        model.addFire(onSaved)
    }
}