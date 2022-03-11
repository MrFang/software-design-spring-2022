package me.fang.kosh.parser.token

/**
 * Токен-переменная. Содержит внутри имя без символа '$'
 */
internal class Var(override val s: String) : StrOrVariable
