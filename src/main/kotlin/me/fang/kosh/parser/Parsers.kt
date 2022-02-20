package me.fang.kosh.parser

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

private val plus = char { c -> c == '+' }
private val minus = char { c -> c == '-' }
private val star = char { c -> c == '*' }
private val slash = char { c -> c == '/' }
private val backSlash = char { c -> c == '\\' }
private val underscore = char { c -> c == '_' }
private val singleQuote = char { c -> c == '\'' }
private val doubleQuote = char { c -> c == '"' }
private val dollar = char { c -> c == '$' }
private val space = char { c -> c == ' ' }
private val equal = char { c -> c == '=' }
private val dot = char { c -> c == '.' }
private val pipelineSeparator = char { c -> c == '|' }
private val comma = char {c -> c == ','}

private val spaces = many(space)

private fun escaped(c: Parser<Char>) = backSlash.then(c)

private val notTerminateChar = char { c -> c.isLetterOrDigit() }
    .or(plus)
    .or(minus)
    .or(star)
    .or(slash)
    .or(underscore)
    .or(dollar)
    .or(equal)
    .or(dot)
    .or(comma)
    .or(escaped(singleQuote))
    .or(escaped(doubleQuote))
    .or(escaped(space))
    .or(escaped(backSlash))
    .or(escaped(pipelineSeparator))
    .or(escaped(dollar))

private val bareStringChar = notTerminateChar
    .or(space)
    .or(pipelineSeparator)

private val bareString: Parser<Token> = some(bareStringChar)
    .map { l -> BareString(l.joinToString("")) }

private val singleQuotedString: Parser<Token> = singleQuote
    .then<Token>(
        many(bareStringChar.or(doubleQuote))
            .map { l -> SingleQuotedString(l.joinToString("")) }
    )
    .before(singleQuote)

private val doubleQuotedString: Parser<Token> = doubleQuote
    .then<Token>(
        many(bareStringChar.or(singleQuote))
            .map { l -> DoubleQuotedString(l.joinToString("")) }
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
    .then(some(notTerminateChar).map { l -> l.joinToString("") })

private val pipe = spaces
    .then(pipelineSeparator)
    .then(some(token))

val commands = Parser { s ->
    when (val res = stringTokens.parse(s)) {
        null -> null
        else -> some(token)
            .and(many(pipe))
            .map { (t, l) -> listOf(t).plus(l) }
            // TODO: Save info about quoting
            .parse(res.second.map { tokenToString(it) }.fold("") { a, v -> "$a$v" })
    }
}
