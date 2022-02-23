package me.fang.kosh.parser.token

/**
 * Строка без кавычек
 */
internal class BareString(override val s: String) : Token {
    override fun toString(): String = "BareString: $s"

    override fun equals(other: Any?): Boolean = if (other is BareString) s == other.s else false

    override fun hashCode(): Int = s.hashCode()
}
