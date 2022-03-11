package me.fang.kosh.exceptions

/**
 * Исключение парсера. В сообщении сохраняет токен, на котором сломался парсинг
 */
class UnexpectedTokenException(s: String) : IllegalStateException("Unexpected token: $s")
