package pt.ulusofona.deisi.cm2122.g21700980_21906966.fire

class Person(
    private val cc: String,
    private val name: String,
    private val apelido : String,
) {

    fun getName() = this.name

    fun getApelido() = this.apelido

    fun getCc() = this.cc

    override fun toString(): String {
        return "$name $apelido [$cc]"
    }
}
