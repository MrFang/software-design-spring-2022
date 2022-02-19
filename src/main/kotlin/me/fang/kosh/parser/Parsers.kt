package me.fang.kosh.parser

import me.fang.kosh.parser.token.BareString
import me.fang.kosh.parser.token.DoubleQuotedString
import me.fang.kosh.parser.token.SingleQuotedString
import me.fang.kosh.parser.token.Token

fun char(p: (Char) -> Boolean): Parser<Char> = Parser { s ->
    if (s.isEmpty() || !p(s[0])) {
        return@Parser null
    } else {
        return@Parser Pair(s.drop(1), s[0])
    }
}

val plus = char { c -> c == '+' }
val minus = char { c -> c == '-' }
val star = char { c -> c == '*' }
val slash = char { c -> c == '/' }
val backSlash = char { c -> c == '\\' }
val underscore = char { c -> c == '_' }
val singleQuote = char { c -> c == '\'' }
val doubleQuote = char { c -> c == '"' }
val dollar = char { c -> c == '$' }
val space = char { c -> c == ' ' }
val equal = char { c -> c == '=' }

val spaces = many(space)

val escapedSingleQuote = backSlash
    .then(singleQuote)
val escapedDoubleQuote = backSlash
    .then(doubleQuote)

val bareStringChar = char { c -> c.isLetterOrDigit() }
    .or(plus)
    .or(minus)
    .or(star)
    .or(slash)
    .or(underscore)
    .or(dollar)
    .or(equal)

val bareString: Parser<Token> = some(bareStringChar)
    .map { l -> BareString(l.joinToString("")) }

val singleQuotedString: Parser<Token> = singleQuote
    .then<Token>(
        many(
            bareStringChar
                .or(space)
                .or(doubleQuote)
                .or(escapedSingleQuote)
        ).map { l -> SingleQuotedString(l.joinToString("")) }
    )
    .before(singleQuote)

val doubleQuotedString: Parser<Token> = doubleQuote
    .then<Token>(
        many(
            bareStringChar
                .or(space)
                .or(singleQuote)
                .or(escapedDoubleQuote)
        ).map { l -> DoubleQuotedString(l.joinToString("")) }
    )
    .before(doubleQuote)

val token = spaces
    .then(bareString.or(singleQuotedString).or(doubleQuotedString))

val pipe = spaces
    .then(char { c -> c == '|' })
    .then(some(token))

val commands = some(token)
    .and(many(pipe))
    .map { (t, l) -> listOf(t).plus(l) }
