package me.fang.kosh.parser.token

/**
 * Интерфейс для различия разных типов строк.
 * Содержит внутри строку, которую оборачивает
 * @property s строка, которую оборачивает токен
 */
internal sealed interface Token {
    val s: String
}

/**
 * Используется для различения строки в одинарных кавычках, двойных кавычках и без кавычек
 */
internal sealed interface QuotationToken : Token

/**
 * Набор различных Type-Union, которые используются, для более точного определения типа при парсинге
 */
internal sealed interface StrOrVariableOrControl : Token

internal sealed interface StrOrControl : StrOrVariableOrControl

internal sealed interface StrOrVariable : StrOrVariableOrControl

internal sealed interface ControlSymbol : StrOrControl
