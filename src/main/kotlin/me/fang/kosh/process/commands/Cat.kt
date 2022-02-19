package me.fang.kosh.process.commands

import me.fang.kosh.process.KoshProcess
import java.io.File

class Cat(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        return args
            .drop(1)
            .map { name -> File(name).readLines().fold("") { text, s -> "$text\n$s" } }
            .fold("") { out, text -> "$out\n\n$text" }
    }
}
