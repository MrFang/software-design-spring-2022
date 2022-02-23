package me.fang.kosh.parser.token

/**
 * Строка в двойных кавычках
 */
internal class DoubleQuotedString(override val s: String) : Token {
    override fun toString(): String = "DoubleQuotedString: $s"

    override fun equals(other: Any?): Boolean = if (other is DoubleQuotedString) s == other.s else false

    override fun hashCode(): Int = s.hashCode()
}
