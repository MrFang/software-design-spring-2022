package me.fang.kosh.process.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import me.fang.kosh.process.KoshProcess
import kotlin.io.path.Path
import kotlin.io.path.readText

class Grep(override val args: List<String>) : KoshProcess, CliktCommand(treatUnknownOptionsAsArgs = true) {
    private var stdinRead: Boolean = false
    private val numberOfLinesAfter: Int by option("-A").int().default(1)
    private val wholeWord: Boolean by option("-w", "--word-regexp").flag(default = false)
    private val caseInsensitive: Boolean by option("-i", "--ignore-case")
        .flag("--no-ignore-case", default = false)
    private val locations: List<String> by argument().multiple()

    init {
        parse(args)
    }

    override fun run() {}

    override fun run(stdin: String): String {
        if (locations.size < 2) {
            return "Usage: grep [OPTION]... PATTERNS [FILE]..."
        }

        val regex = makeRegex(locations[1])

        if (locations.size == 2) {
            return getMatches(stdin, regex)
        }

        return locations
            .drop(2)
            .map { fileName ->
                if (fileName == "-" && !stdinRead) {
                    stdinRead = true
                    getMatches(stdin, regex)
                } else if (stdinRead) {
                    ""
                } else {
                    getMatches(Path(fileName).readText(), regex)
                }
            }
            .fold("") { a, v -> "$a\n$v" }
            .drop(1)
    }

    private fun getMatches(input: String, regex: Regex): String {
        var counter = 0
        return input
            .split('\n')
            .filter { line ->
                run {
                    if (counter > 0) {
                        counter -= 1
                    }

                    if (regex.find(line) != null) {
                        counter = numberOfLinesAfter
                        return@run true
                    } else return@run counter > 0
                }
            }
            .fold("") { a, v -> a + '\n' + v }
            .drop(1) + '\n'
    }

    private fun makeRegex(regex: String) = Regex(
        if (wholeWord) "(^|\\W)$regex(\\W|\$)" else regex,
        if (caseInsensitive) setOf(RegexOption.IGNORE_CASE) else emptySet()
    )
}
