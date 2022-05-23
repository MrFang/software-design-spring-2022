package me.fang.kosh.process.commands

import me.fang.kosh.process.KoshProcess
import java.io.InputStream
import java.io.OutputStream

/**
 * Команда echo
 */
class Echo(override val args: List<String>) : KoshProcess {
    private val allowedOptions = listOf('n', 'e', 'E')
    private var backslashInterpretation = false
    private var trailingNewline = true

    /**
     * Парсит аргументы и возвращает их через пробел в stdout в порядке ввода
     */
    override fun run(stdin: InputStream, stdout: OutputStream, stderr: OutputStream): Int {
        val toPrint = args.drop(1).filter { !it.startsWith('-') || !it.drop(1).containsOf(allowedOptions) }
        args.drop(1).filter { it.startsWith('-') && it.drop(1).containsOf(allowedOptions) }.forEach { arg ->
            arg.drop(1).forEach {
                when (it) {
                    'n' -> trailingNewline = false
                    'e' -> backslashInterpretation = true
                    'E' -> backslashInterpretation = false
                }
            }
        }

        stdout.write(
            toPrint
                .joinToString(
                    separator = " ",
                    postfix = (if (trailingNewline) "\n" else "")
                ) { s -> if (backslashInterpretation) replaceBackslashes(s) else s }
                .toByteArray()
        )

        return 0
    }

    private fun replaceBackslashes(str: String) = str
        .replace("\\n", "\n")
        .replace("\\r", "\r")
        .replace("\\b", "\b")
        .replace("\\t", "\t")
}

private fun CharSequence.containsOf(chars: List<Char>): Boolean = this.all { chars.contains(it) }
