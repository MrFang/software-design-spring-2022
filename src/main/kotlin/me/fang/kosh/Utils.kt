package me.fang.kosh

import me.fang.kosh.parser.token.SingleQuotedString
import me.fang.kosh.parser.token.Token

fun String.applyEnv(): String {
    val vars = Environment.vars
    var result = this

    "\\$[a-zA-z_][a-zA-Z0-9_]*".toRegex() // $varname
        .findAll(this)
        .forEach {
            run {
                val varName = it.value.slice(1 until it.value.length)
                result = result.replace("\$$varName", vars[varName] ?: "")
            }
        }

    return result.trim()
}

fun tokenToString(t: Token): String = when (t) {
    is SingleQuotedString -> t.s
    else -> t.s.applyEnv()
}
