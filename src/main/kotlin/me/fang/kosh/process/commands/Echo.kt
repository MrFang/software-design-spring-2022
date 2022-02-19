package me.fang.kosh.process.commands

import me.fang.kosh.process.KoshProcess

class Echo(override val args: List<String>) : KoshProcess {
    // TODO: Process keys
    override fun run(stdin: String): String = args.drop(1).fold("") { acc, s -> "$acc $s" }
}
