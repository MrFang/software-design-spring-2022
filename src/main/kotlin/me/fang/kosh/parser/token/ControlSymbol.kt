package me.fang.kosh.parser.token

/**
 * Классы различных управляющих символов.
 * На данный момент поддерживаются только пробелы и pipeline-разделители
 */
internal class Space(override val s: String) : ControlSymbol
internal class PipelineSeparator(override val s: String) : ControlSymbol
