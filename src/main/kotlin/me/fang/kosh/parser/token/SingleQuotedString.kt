package me.fang.kosh.parser.token

class SingleQuotedString(override val s: String) : Token {
    override fun toString(): String = "SingleQuotedString: $s"
}
