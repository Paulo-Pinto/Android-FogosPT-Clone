package pt.ulusofona.deisi.cm2122.g21700980_21906966.fire

class Person(
    private val name: String,
    private val cc: String,
) {

    fun getName() = this.name

    fun getCc() = this.cc

    override fun toString(): String {
        return "$name [$cc]"
    }
}
