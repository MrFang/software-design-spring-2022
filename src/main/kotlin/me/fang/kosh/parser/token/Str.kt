package me.fang.kosh.parser.token

/**
 * Токен не являющийся переменной или управляющим символом
 */
internal class Str(override val s: String) : StrOrControl, StrOrVariable
