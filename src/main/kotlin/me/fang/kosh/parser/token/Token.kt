package me.fang.kosh.parser.token

/**
 * Интерфейс для различия разных типов строк.
 * Содержит внутри строку, которую оборачивает
 * @property s строка, которую оборачивает токен
 */
sealed interface Token {
    val s: String
}

internal sealed interface QuotationToken : Token

internal sealed interface StrOrVariableOrControl : Token

internal sealed interface StrOrControl : StrOrVariableOrControl

internal sealed interface StrOrVariable : StrOrVariableOrControl

internal sealed interface ControlSymbol : StrOrControl
