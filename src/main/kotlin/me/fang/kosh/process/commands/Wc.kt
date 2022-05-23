package me.fang.kosh.process.commands

import me.fang.kosh.getResource
import me.fang.kosh.process.KoshProcess
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.readText

/**
 * Команда wc
 */
class Wc(override val args: List<String>) : KoshProcess {
    private var help = false
    private var defaultOutput = true
    private var wc = false
    private var lc = false
    private var cc = false
    private var bc = false
    private var dw = false
    private var fromFile: String? = null
    private var stdinRead = false

    /**
     * Забирает из аргументов имена файлов, читает их и считает по ним число
     * слов, переводов строки, символов, байт и максимальную длину строки.
     * Возвращает посчитанные метрики в качестве строки
     */
    override fun run(stdin: InputStream, stdout: OutputStream, stderr: OutputStream): Int {
        var filenames = args.drop(1).filter { it == "-" || !it.startsWith('-') }

        args.drop(1).filter { it.startsWith('-') && it != "-" }.forEach { arg ->
            run {
                if (arg.startsWith("--files0-from=")) {
                    fromFile = arg.split('=', limit = 1)[1]
                } else if (!arg.startsWith("--")) {
                    defaultOutput = false
                    arg.drop(1).forEach {
                        when (it) {
                            'c' -> bc = true
                            'm' -> cc = true
                            'l' -> lc = true
                            'L' -> dw = true
                            'w' -> wc = true
                            else -> {
                                stderr.write("invalid option: '$it'".toByteArray())
                                return 1
                            }
                        }
                    }
                } else {
                    when (arg.drop(2)) {
                        "help" -> { help = true; return@forEach }
                        "bytes" -> { bc = true; defaultOutput = false }
                        "chars" -> { cc = true; defaultOutput = false }
                        "lines" -> { lc = true; defaultOutput = false }
                        "max-line-length" -> { dw = true; defaultOutput = false }
                        "words" -> { wc = true; defaultOutput = false }
                        else -> {
                            stderr.write("Invalid option: '$arg'".toByteArray())
                            return 1
                        }
                    }
                }
            }
        }

        if (defaultOutput) {
            lc = true
            wc = true
            bc = true
        }

        if (help) {
            val file = getResource("/messages/commands-help/wc.txt")
            return if (file == null) {
                stderr.write("Internal error".toByteArray())
                1
            } else {
                stdout.write(file.readText().toByteArray())
                0
            }
        }

        if (fromFile != null) {
            filenames = if (fromFile == "-") {
                stdin.readBytes()
                    .toString(Charset.defaultCharset())
                    .split(0.toChar())
            } else {
                Path(fromFile!!).readText().split(0.toChar())
            }
        }

        stdout.write(
            (
                if (filenames.isNotEmpty()) {
                    filenames.fold("") { out, name ->
                        run {
                            val text = if (name == "-") {
                                stdin.readAllBytes().toString(Charset.defaultCharset())
                            } else {
                                val f = Path(name)
                                if (f.isDirectory()) {
                                    stderr.write("$name is directory".toByteArray())
                                }
                                f.readText()
                            }

                            "${out}\n" + getOutput(text) + name
                        }
                    }.drop(1)
                } else {
                    getOutput(stdin.readBytes().toString(Charset.defaultCharset())).dropLast(1)
                } + '\n'
                ).toByteArray()
        )

        return 0
    }

    private fun getOutput(text: String): String = buildString {
        append(if (lc) "${getLinesCount(text)} " else "")
        append(if (wc) "${getWordsCount(text)} " else "")
        append(if (cc) "${getCharsCount(text)} " else "")
        append(if (bc) "${getBytesCount(text)} " else "")
        append(if (dw) "${getDisplayWidth(text)} " else "")
    }

    private fun getWordsCount(str: String): Int = str.split(' ').filter { it.isNotEmpty() }.size

    private fun getLinesCount(str: String): Int = str.split('\n').size

    private fun getCharsCount(str: String): Long = str.chars().count()

    private fun getBytesCount(str: String): Int = str.toByteArray().size

    private fun getDisplayWidth(str: String): Int = str.split('\n').maxOf { s -> s.length }
}
