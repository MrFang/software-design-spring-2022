package me.fang.kosh.parser

class Parser<Token>(private val f: (String) -> Pair<String, Token>?) {
    fun parse(s: String): Pair<String, Token>? = f(s)

    fun <Token2> then(other: Parser<Token2>): Parser<Token2> = this
        .and(other)
        .map { (_, t) -> t }

    fun <Token2> and(other: Parser<Token2>): Parser<Pair<Token, Token2>> = Parser { s ->
        when (val res = parse(s)) {
            null -> null
            else -> when (val res2 = other.parse(res.first)) {
                null -> null
                else -> Pair(res2.first, Pair(res.second, res2.second))
            }
        }
    }

    fun <Token2> map(g: (Token) -> Token2): Parser<Token2> = Parser { s ->
        when (val res = parse(s)) {
            null -> null
            else -> Pair(res.first, g(res.second))
        }
    }

    fun or(other: Parser<Token>): Parser<Token> = Parser { s ->
        when (val res = parse(s)) {
            null -> other.parse(s)
            else -> res
        }
    }
}

fun <Token> many(p: Parser<Token>): Parser<List<Token>> = Parser { s ->
    run {
        var res = listOf<Token>()
        var rem = s
        var maybePair = p.parse(rem)

        while (maybePair != null && maybePair.first.length < rem.length) {
            rem = maybePair.first
            res = res.plus(maybePair.second)
            maybePair = p.parse(rem)
        }
        return@Parser Pair(rem, res)
    }
}

fun <Token> some(p: Parser<Token>): Parser<List<Token>> = Parser { s ->
    when (val res = p.parse(s)) {
        null -> null
        else -> many(p).map { l -> listOf(res.second).plus(l) }.parse(res.first)
    }
}
