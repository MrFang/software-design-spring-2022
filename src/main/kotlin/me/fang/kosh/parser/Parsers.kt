package me.fang.kosh.parser

import me.fang.kosh.Environment
import me.fang.kosh.exceptions.UnexpectedTokenException
import me.fang.kosh.parser.token.BareString
import me.fang.kosh.parser.token.ControlSymbol
import me.fang.kosh.parser.token.DoubleQuotedString
import me.fang.kosh.parser.token.PipelineSeparator
import me.fang.kosh.parser.token.QuotationToken
import me.fang.kosh.parser.token.SingleQuotedString
import me.fang.kosh.parser.token.Space
import me.fang.kosh.parser.token.Str
import me.fang.kosh.parser.token.StrOrControl
import me.fang.kosh.parser.token.StrOrVariable
import me.fang.kosh.parser.token.StrOrVariableOrControl
import me.fang.kosh.parser.token.Var

private fun char(p: (Char) -> Boolean): Parser<String, Char> = Parser { s ->
    if (s.isEmpty()) {
        Result.failure(UnexpectedTokenException("<empty string>"))
    } else if (!p(s.first())) {
        Result.failure(UnexpectedTokenException(s.first().toString()))
    } else {
        Result.success(Pair(s.drop(1), s.first()))
    }
}

private fun tok(p: (StrOrControl) -> Boolean): Parser<List<StrOrControl>, StrOrControl> = Parser { l ->
    if (l.isEmpty()) {
        Result.failure(UnexpectedTokenException("<empty list>"))
    } else if (!p(l.first())) {
        Result.failure(UnexpectedTokenException(l.first().s))
    } else {
        Result.success(Pair(l.drop(1), l[0]))
    }
}

private const val CONTROLS = "|&;()<> "

private val backSlash = char { it == '\\' }
private val singleQuote = char { it == '\'' }
private val doubleQuote = char { it == '"' }
private val dollar = char { it == '$' }
private fun escaped(c: Parser<String, Char>) = backSlash.then(c)
private val controlChar = char { CONTROLS.contains(it) }
private val nonControlChar = escaped(controlChar).or(char { !CONTROLS.contains(it) })

private val spaceToken = char { it == ' ' }.map<ControlSymbol> { Space(it.toString()) }
private val pipeToken = many(spaceToken)
    .then(char { it == '|' }.map<ControlSymbol> { PipelineSeparator(it.toString()) })
private val controlToken = spaceToken.or(pipeToken) // TODO: При расширении сюда можно добавить остальные управляющие последовательности

private val variable = dollar
    .then(char { it.isLetter() })
    .and(
        many(char { it.isLetterOrDigit() })
            .map { it.joinToString("") }
    ).map { (h, t) -> h + t }

private val bareString: Parser<String, QuotationToken> = some(
    escaped(singleQuote)
        .or(escaped(doubleQuote))
        .or(char { it != '"' && it != '\'' })
)
    .map { it.joinToString("") }
    .map { BareString(it) }

private val doubleQuotedString: Parser<String, QuotationToken> = doubleQuote
    .then(many(escaped(doubleQuote).or(char { it != '"' })))
    .before(doubleQuote)
    .map { it.joinToString("") }
    .map { DoubleQuotedString(it) }

private val singleQuotedString: Parser<String, QuotationToken> = singleQuote
    .then(many(escaped(singleQuote).or(char { it != '\'' })))
    .before(singleQuote)
    .map { it.joinToString("") }
    .map { SingleQuotedString(it) }

private val quotedStrings = many(bareString.or(doubleQuotedString).or(singleQuotedString))

private val nonControlToken = some(nonControlChar).map { it.joinToString("") }

private val bareStringTokens = many(
    (nonControlToken.map<StrOrControl> { Str(it) })
        .or(controlToken.map { it }) // Для правильного type-inference
)

private val stringWithVars = some(
    escaped(char { true }).map<StrOrVariable> { Str(it.toString()) }
        .or(variable.map { Var(it) })
        .or(char { it != '$' }.map { Str(it.toString()) })
)

private val commandToken = many(tok { it is Space })
    .then(
        some(tok { it is Str })
            .map { it.reduce { a, v -> Str(a.s + v.s) } }
    )

private val cmdWithArgs = some(commandToken)

private val pipe = many(tok { it is Space })
    .then(tok { it is PipelineSeparator })
    .then(cmdWithArgs)

private val commands = cmdWithArgs
    .and(many(pipe))
    .map { (h, t) -> listOf(h).plus(t) }

private fun preTokenList(str: String): Result<List<StrOrControl>> {
    val quoted = quotedStrings.parse(str).getOrElse { return Result.failure(it) }

    val result: MutableList<StrOrVariableOrControl> = mutableListOf()

    for (s in quoted.second) {
        when (s) {
            is SingleQuotedString -> result.add(Str(s.s))
            is DoubleQuotedString -> {
                val stringsAndVars = stringWithVars.parse(s.s).getOrElse { return Result.failure(it) }
                result.addAll(stringsAndVars.second)
            }
            is BareString -> {
                val tokens = bareStringTokens.parse(s.s).getOrElse { return Result.failure(it) }

                for (t in tokens.second) {
                    when (t) {
                        is ControlSymbol -> result.add(t)
                        is Str -> {
                            val stringsAndVars = stringWithVars.parse(t.s).getOrElse { return Result.failure(it) }
                            result.addAll(stringsAndVars.second)
                        }
                    }
                }
            }
        }
    }

    return Result.success(
        result.map { t ->
            when (t) {
                is Var -> Str(Environment.vars.getOrDefault(t.s, ""))
                is Str -> t
                is ControlSymbol -> t
            }
        }
    )
}

/**
 * Парсит переданную строку и возвращает список команд, каждая из которых представляется списком из имени и аргументов
 * @param s входная строка
 * @return Список команд или failure, если парсинг не удался
 */
internal fun parse(s: String): Result<List<List<String>>> {
    val preTokens = preTokenList(s).getOrElse { return Result.failure(it) }
    val res = commands.parse(preTokens).getOrElse { return Result.failure(it) }

    if (res.first.isNotEmpty()) {
        return Result.failure(UnexpectedTokenException(res.first.dropWhile { it is Space }.first().s))
    }

    return Result.success(res.second.map { l -> l.map { it.s } })
}
