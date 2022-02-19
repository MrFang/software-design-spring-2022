package me.fang.kosh.parser

fun char(p: (Char) -> Boolean): Parser<Char> = Parser { s ->
    if (s.isEmpty() || !p(s[0])) {
        return@Parser null
    } else {
        Pair(s.slice(1 until s.length), s[0])
    }
}

val spaces = many(char { c -> c == ' ' })

val word = some(char { c -> c.isLetter() })
    .map { l -> l.joinToString("") }

val token = spaces
    .then(word)

val pipe = spaces
    .then(char { c -> c == '|' })
    .then(some(token))

val commands = some(token)
    .and(many(pipe))
    .map { (t, l) -> listOf(t).plus(l) }
