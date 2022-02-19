package me.fang.kosh.parser.token

class BareString(override val s: String) : Token {
    override fun toString(): String = "BareString: $s"
}
