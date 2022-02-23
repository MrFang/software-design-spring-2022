package me.fang.kosh.parser

/**
 * Реализация парсер-комбинатора.
 * @param Token -- тип результата парсинга
 * @param f функция принимающая строку и возвращающая пару из остатка строки и резульатата парсинга
 * или null, если парсинг не удался
 */
internal class Parser<Token>(private val f: (String) -> Pair<String, Token>?) {
    /**
     * Запускает парсинг данной строки.
     * @param s строка для парсинга
     * @return результат парсинга или null, если парсинг не удался
     */
    fun parse(s: String): Pair<String, Token>? = f(s)

    /**
     * Запускает на строке сначала `this` парсер, а потом переданный, на остатке строки.
     * Возвращает null, если падает хотя бы один из парсеров.
     * Отбрасывает результат первого парсера
     * @param Token2 тип возвращаемого результата второго парсера
     * @param other парсер для запуска на остатке строки
     * @return парсер, который запускает на строке последовательно два парсера и вернёт результат вторго
     * @see [before]
     * @see [and]
     */
    fun <Token2> then(other: Parser<Token2>): Parser<Token2> = this
        .and(other)
        .map { (_, t) -> t }

    /**
     * Запускает сначала `this` парсер, а затем переданный на остатке строки.
     * Возврашает null, если упадёт хотя бы один парсер.
     * Отбрасывает результат второго парсера.
     * @param Token2 тип возвращаемого результата второго парсера
     * @param other парсер для запуска на остатке строки
     * @return парсер, который запустит последовательно два парсера и вернёт результат первого
     * @see [then]
     * @see [and]
     */
    fun <Token2> before(other: Parser<Token2>): Parser<Token> = this
        .and(other)
        .map { (t, _) -> t }

    /**
     * Запускает сначала `this` парсер, а затем переданный на остатке строки.
     * Возвращает null, если падает хотя бы один парсер.
     * Возвращает парсер, содержащий пару из результатов первого и второго парсера.
     * @param Token2 тип возвращаемого результата второго парсера
     * @param other парсер для запуска на остатке строки
     * @return парсер, который запустит последовательно два парсера и соберёт резульат в пару
     * @see [before]
     * @see [then]
     * @see [or]
     */
    fun <Token2> and(other: Parser<Token2>): Parser<Pair<Token, Token2>> = Parser { s ->
        when (val res = parse(s)) {
            null -> null
            else -> when (val res2 = other.parse(res.first)) {
                null -> null
                else -> Pair(res2.first, Pair(res.second, res2.second))
            }
        }
    }

    /**
     * Запускает переданную функцию на успешном результате парсера.
     * Возвращает парсер с возвращённым занчением функции
     * @param Token2 возвращаемый результат функции
     * @param g функция преобразования результата
     * @return парсер с преобразованным резульатом
     */
    fun <Token2> map(g: (Token) -> Token2): Parser<Token2> = Parser { s ->
        when (val res = parse(s)) {
            null -> null
            else -> Pair(res.first, g(res.second))
        }
    }

    /**
     * Запускает `this` парсер и, если он падает, запускает переданный на той же строке.
     * @param other парсер, который нужно запустить в случае неудачи
     * @return парсер с результатом первого или второго парсера
     * @see [and]
     */
    fun or(other: Parser<Token>): Parser<Token> = Parser { s ->
        when (val res = parse(s)) {
            null -> other.parse(s)
            else -> res
        }
    }
}

/**
 * Принимает парсер и парсет им строку до первой неудачи.
 * Возвращает нуль или более успехов.
 * @param Token тип резульата парсера
 * @param p парсер для запуска на строке
 * @return парсер со списком распашенных результатов. Список может быть пустым,
 * поэтому результирующий парсер всегда успешен
 * @see [some]
 */
internal fun <Token> many(p: Parser<Token>): Parser<List<Token>> = Parser { s ->
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

/**
 * Принимает парсер и запускает его на строке до первой неудачи.
 * Возвращает один или более успхов.
 * @param Token тип резульата парсера
 * @param p парсер для запуска на строке
 * @return парсер со списком распаршенных резульатов. Список сожержит хотя бы один элемент
 * @see [many]
 */
internal fun <Token> some(p: Parser<Token>): Parser<List<Token>> = Parser { s ->
    when (val res = p.parse(s)) {
        null -> null
        else -> many(p).map { l -> listOf(res.second).plus(l) }.parse(res.first)
    }
}
