package me.fang.kosh.parser.token

class SingleQuotedString(override val s: String) : Token {
    override fun toString(): String = "SingleQuotedString: $s"

    override fun equals(other: Any?): Boolean = if (other is SingleQuotedString) s == other.s else false

    override fun hashCode(): Int = s.hashCode()
}
