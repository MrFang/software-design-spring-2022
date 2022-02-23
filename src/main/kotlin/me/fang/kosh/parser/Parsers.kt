package me.fang.kosh.parser

import me.fang.kosh.Environment
import me.fang.kosh.parser.token.BareString
import me.fang.kosh.parser.token.DoubleQuotedString
import me.fang.kosh.parser.token.SingleQuotedString
import me.fang.kosh.parser.token.Token

private fun char(p: (Char) -> Boolean): Parser<Char> = Parser { s ->
    if (s.isEmpty() || !p(s[0])) {
        return@Parser null
    } else {
        return@Parser Pair(s.drop(1), s[0])
    }
}

private val backSlash = char { it == '\\' }
private val singleQuote = char { it == '\'' }
private val doubleQuote = char { it == '"' }
private val space = char { it == ' ' }
private val pipelineSeparator = char { it == '|' }

private val spaces = many(space)

private fun escaped(c: Parser<Char>) = backSlash.then(c)

private const val terminals = "|&;()<>\\ \n\t'\""

private val terminalChar = char { terminals.contains(it) }

private val notTerminalChar = char { !terminals.contains(it) }
    .or(escaped(terminalChar))

private val bareString: Parser<Token> = some(
    notTerminalChar
        .or(space)
        .or(pipelineSeparator)
)
    .map { l -> BareString(l.joinToString("")) }

private val singleQuotedString: Parser<Token> = singleQuote
    .then<Token>(
        many(char { it != '\'' }.or(escaped(singleQuote)))
            .map { SingleQuotedString(it.joinToString("")) }
    )
    .before(singleQuote)

private val doubleQuotedString: Parser<Token> = doubleQuote
    .then<Token>(
        many(char { it != '"' }.or(escaped(doubleQuote)))
            .map { DoubleQuotedString(it.joinToString("")) }
    )
    .before(doubleQuote)

private val stringTokens = many(bareString.or(doubleQuotedString).or(singleQuotedString))

private fun String.escapeControlSymbols(): String = this
    .replace("\\", "\\\\")
    .replace(" ", "\\ ")
    .replace("|", "\\|")
    .replace("\"", "\\\"")
    .replace("'", "\\'")

private fun tokenToString(token: Token): String = when (token) {
    is BareString -> token.s
    is DoubleQuotedString -> token.s.escapeControlSymbols()
    is SingleQuotedString -> token.s.escapeControlSymbols()
}

private val token = spaces
    .then(some(notTerminalChar).map { it.joinToString("") })

private val pipe = spaces
    .then(pipelineSeparator)
    .then(some(token))

private val pipeline = some(token)
    .and(many(pipe))
    .map { (t, l) -> listOf(t).plus(l) }

private fun String.applyEnv(): String {
    val vars = Environment.vars
    var result = this

    "\\$[a-zA-z_][a-zA-Z0-9_]*".toRegex() // $var_name
        .findAll(this)
        .forEach {
            run {
                val varName = it.value.drop(1)
                result = result.replace("\$$varName", vars[varName] ?: "")
            }
        }

    return result
}

val commands = Parser { s ->
    when (val res = stringTokens.parse(s)) {
        null -> null
        else -> pipeline.parse(
            res.second.fold("") { str, token ->
                when (token) {
                    is SingleQuotedString -> "$str${tokenToString(token)}"
                    else -> "$str${tokenToString(token).applyEnv()}"
                }
            }
        )
    }
}
