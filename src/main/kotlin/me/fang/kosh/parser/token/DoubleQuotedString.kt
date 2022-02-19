package me.fang.kosh.parser.token

class DoubleQuotedString(override val s: String) : Token {
    override fun toString(): String = "DoubleQuotedString: $s"
}
