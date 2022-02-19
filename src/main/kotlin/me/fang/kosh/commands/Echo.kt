package me.fang.kosh.commands

class Echo(private val args: List<String>) {
    // TODO: Process keys
    fun run(stdin: String = ""): String = args.reduce { acc, s -> "$acc $s" }
}
