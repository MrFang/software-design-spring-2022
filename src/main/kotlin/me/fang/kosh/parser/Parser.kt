package me.fang.kosh.parser

/**
 * Реализация парсер-комбинатора.
 * @param Input -- Тип входного потока
 * @param Token -- тип результата парсинга
 * @param f функция принимающая строку и возвращающая пару из остатка строки и результата парсинга
 * или failure, если парсинг не удался
 * @see[me.fang.kosh.exceptions.UnexpectedTokenException]
 */
internal class Parser<Input, Token>(private val f: (Input) -> Result<Pair<Input, Token>>) {
    /**
     * Запускает парсинг данной строки.
     * @param s строка для парсинга
     * @return Результат парсинга или failure, если парсинг не удался
     */
    fun parse(s: Input): Result<Pair<Input, Token>> = f(s)

    /**
     * Запускает на строке сначала `this` парсер, а потом переданный, на остатке строки.
     * Возвращает failure, если падает хотя бы один из парсеров.
     * Отбрасывает результат первого парсера
     * @param Token2 тип возвращаемого результата второго парсера
     * @param other парсер для запуска на остатке строки
     * @return Парсер, который запускает на строке последовательно два парсера и вернёт результат второго
     * @see [before]
     * @see [and]
     */
    fun <Token2> then(other: Parser<Input, Token2>): Parser<Input, Token2> = this
        .and(other)
        .map { (_, t) -> t }

    /**
     * Запускает сначала `this` парсер, а затем переданный на остатке строки.
     * Возвращает failure, если упадёт хотя бы один парсер.
     * Отбрасывает результат второго парсера.
     * @param Token2 тип возвращаемого результата второго парсера
     * @param other парсер для запуска на остатке строки
     * @return Парсер, который запустит последовательно два парсера и вернёт результат первого
     * @see [then]
     * @see [and]
     */
    fun <Token2> before(other: Parser<Input, Token2>): Parser<Input, Token> = this
        .and(other)
        .map { (t, _) -> t }

    /**
     * Запускает сначала `this` парсер, а затем переданный на остатке строки.
     * Возвращает failure, если падает хотя бы один парсер.
     * Возвращает парсер, содержащий пару из результатов первого и второго парсера.
     * @param Token2 тип возвращаемого результата второго парсера
     * @param other парсер для запуска на остатке строки
     * @return Парсер, который запустит последовательно два парсера и соберёт результат в пару
     * @see [before]
     * @see [then]
     * @see [or]
     */
    fun <Token2> and(other: Parser<Input, Token2>): Parser<Input, Pair<Token, Token2>> = Parser { s ->
        val res = parse(s).getOrElse { return@Parser Result.failure(it) }
        val res2 = other.parse(res.first).getOrElse { return@Parser Result.failure(it) }
        Result.success(Pair(res2.first, Pair(res.second, res2.second)))
    }

    /**
     * Запускает переданную функцию на успешном результате парсера.
     * Возвращает парсер с возвращённым значением функции
     * @param Token2 возвращаемый результат функции
     * @param g функция преобразования результата
     * @return Парсер с преобразованным результатом
     */
    fun <Token2> map(g: (Token) -> Token2): Parser<Input, Token2> = Parser { s ->
        parse(s).mapCatching { (i, r) -> Pair(i, g(r)) }
    }

    /**
     * Запускает `this` парсер и, если он падает, запускает переданный на той же строке.
     * @param other парсер, который нужно запустить в случае неудачи
     * @return Парсер с результатом первого или второго парсера
     * @see [and]
     */
    fun or(other: Parser<Input, Token>): Parser<Input, Token> = Parser { s ->
        val res = parse(s)

        if (res.isSuccess) {
            res
        } else {
            other.parse(s)
        }
    }
}

/**
 * Принимает парсер и парсит им строку до первой неудачи.
 * Возвращает нуль или более успехов.
 * @param Input тип входного потока парсера.
 * ВАЖНО: итерация парсинга считается успешной, если
 * остаток "после" не равен остатку "до" по оператору !=
 * @param Token тип результата парсера
 * @param p парсер для запуска на строке
 * @return Парсер со списком распаршенных результатов. Список может быть пустым,
 * поэтому результирующий парсер всегда успешен
 * @see [some]
 */
internal fun <Input, Token> many(p: Parser<Input, Token>): Parser<Input, List<Token>> = Parser { s ->
    run {
        var res = listOf<Token>()
        var rem = s
        var maybePair = p.parse(rem)

        while (maybePair.isSuccess && maybePair.getOrThrow() != rem) {
            rem = maybePair.getOrThrow().first
            res = res.plus(maybePair.getOrThrow().second)
            maybePair = p.parse(rem)
        }
        return@Parser Result.success(Pair(rem, res))
    }
}

/**
 * Принимает парсер и запускает его на строке до первой неудачи.
 * Возвращает один или более успхов.
 * @param Input тип входного потока парсера
 * @param Token тип результата парсера
 * @param p парсер для запуска на строке
 * @return Парсер со списком распаршенных результатов. Список содержит хотя бы один элемент
 * @see [many]
 */
internal fun <Input, Token> some(p: Parser<Input, Token>): Parser<Input, List<Token>> = Parser { s ->
    val res = p.parse(s).getOrElse { return@Parser Result.failure(it) }
    many(p).map { l -> listOf(res.second).plus(l) }.parse(res.first)
}
