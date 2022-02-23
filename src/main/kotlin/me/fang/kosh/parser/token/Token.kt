package me.fang.kosh.parser.token

/**
 * Интерфейс для различия разных типов строк.
 * Содержит внутри строку, которую оборачивает
 * @property s Строка, которую оборачивает токен
 */
sealed interface Token {
    val s: String
}
