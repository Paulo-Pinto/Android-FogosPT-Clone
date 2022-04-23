package pt.ulusofona.deisi.cm2122.g21700980_21906966

import androidx.lifecycle.ViewModel

class RegisterFireViewModel : ViewModel() {

    private val model = Fogospt

    fun setLocation(district: String, county: String, parish: String): String =
        model.setLocation(district, county, parish)

    fun setSubmitter(person: Person): String = model.setSubmitter(person)

    fun setObservations(obs: String) = model.setObservations(obs)

    fun setState(s: String) = model.setState(s)

    fun setRandomState() = model.setRandomState()

    fun onGetFireList(onFinished: (List<Fire>) -> Unit) {
        model.getFireList(onFinished)
    }

    fun addFire(onSaved: () -> Unit) {
        model.addFire(onSaved)
    }
}