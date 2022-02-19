package me.fang.kosh.process.commands

import me.fang.kosh.process.KoshProcess
import java.io.File

class Wc(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        return args
            .drop(1)
            .map { File(it).readText(Charsets.UTF_8).split(' ').size }
            .fold("") { acc, count -> "$acc\n$count" }
    }
}
