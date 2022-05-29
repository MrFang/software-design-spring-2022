package me.fang.kosh.process.commands

import me.fang.kosh.getResource
import me.fang.kosh.process.Cli
import me.fang.kosh.process.KoshProcess
import java.nio.charset.Charset
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Команда cat
 */
class Cat(override val args: List<String>) : KoshProcess {
    private var help = false
    private var numberNonEmptyLines = false
    private var showEndOfLine = false
    private var numberAllLines = false
    private var suppressEmptyLines = false
    private var showTabs = false
    private var showNonPrinting = false

    /**
     * Парсит stdin, находит в нём имена файлов и выводит их содержимое в порядке ввода в stdout
     */
    override fun run(cli: Cli): Int {
        val filenames = args.drop(1).filter { it == "-" || !it.startsWith('-') }
        val res = parseArgs(args)

        if (res.isFailure) {
            cli.stderr.write(res.exceptionOrNull()!!.toString().toByteArray())
            return 1
        }

        if (help) {
            val file = getResource("/messages/commands-help/cat.txt")
            return if (file == null) {
                cli.stderr.write("Internal error".toByteArray())
                1
            } else {
                cli.stdout.write(file.readText().toByteArray())
                0
            }
        }

        if (filenames.isEmpty()) {
            cli.stderr.write("No args passed".toByteArray())
            return 1
        }

        cli.stdout.write(
            (
                if (filenames.isNotEmpty()) {
                    filenames.fold("") { out, file ->
                        "$out\n${
                        if (file == "-") {
                            transform(cli.stdin.readBytes().toString(Charset.defaultCharset()))
                        } else {
                            transform(Path(file).readText())
                        }}"
                    }
                        .drop(1)
                } else {
                    transform(cli.stdin.readBytes().toString(Charset.defaultCharset()))
                }
                )
                .toByteArray()
        )

        return 0
    }

    private fun parseArgs(args: List<String>): Result<Unit> {
        args.drop(1).filter { it.startsWith('-') && it != "-" }.forEach { arg ->
            run {
                if (!arg.startsWith("--")) {
                    arg.drop(1).forEach {
                        when (it) {
                            'A' -> {
                                showNonPrinting = true
                                showTabs = true
                                showEndOfLine = true
                            }
                            'b' -> {
                                numberNonEmptyLines = true
                                numberAllLines = false
                            }
                            'e' -> {
                                showNonPrinting = true
                                showEndOfLine = true
                            }
                            'E' -> showEndOfLine = true
                            'n' -> if (!numberNonEmptyLines) numberAllLines = true
                            's' -> suppressEmptyLines = true
                            't' -> {
                                showTabs = true
                                showNonPrinting = true
                            }
                            'T' -> showTabs = true
                            'u' -> {}
                            'v' -> showNonPrinting = true
                            else -> {
                                return Result.failure(IllegalStateException("Invalid option: '$it'"))
                            }
                        }
                    }
                } else {
                    when (arg.drop(2)) {
                        "show-all" -> {
                            showNonPrinting = true
                            showTabs = true
                            showEndOfLine = true
                        }
                        "number-nonblank" -> {
                            numberNonEmptyLines = true
                            numberAllLines = false
                        }
                        "show-ends" -> showEndOfLine = true
                        "number" -> if (!numberNonEmptyLines) numberAllLines = true
                        "squeeze-blank" -> suppressEmptyLines = true
                        "show-tabs" -> showTabs = true
                        "show-nonprinting" -> showNonPrinting = true
                        "help" -> {
                            help = true
                            return@forEach
                        }
                        else -> {
                            return Result.failure(IllegalStateException("Invalid option: '$arg'"))
                        }
                    }
                }
            }
        }

        return Result.success(Unit)
    }

    private fun transform(txt: String): String {
        var text = txt

        if (suppressEmptyLines) {
            text = text
                .split('\n')
                .fold("") { acc, line ->
                    if (line == "" && acc.endsWith("\n")) {
                        acc
                    } else {
                        "$acc\n$line"
                    }
                }
                .drop(1)
        }

        if (showEndOfLine) {
            text = text
                .split('\n')
                .fold("") { acc, line -> "$acc\n$line\$" }
                .drop(1)
        }

        if (numberNonEmptyLines) {
            var lineNum = 0
            text = text
                .split('\n')
                .fold("") { acc, line ->
                    if (line != "") {
                        "$acc\n${++lineNum} $line"
                    } else {
                        "$acc\n"
                    }
                }
                .drop(1)
        } else if (numberAllLines) {
            text = text
                .split('\n')
                .withIndex()
                .fold("") { acc, (i, line) -> "$acc\n${i + 1}${if (line.isEmpty()) "" else " $line"}" }
                .drop(1)
        }

        if (showTabs) {
            // TODO: Replace tabs
        }

        if (showNonPrinting) {
            // TODO: Replace
        }

        return text
    }
}
