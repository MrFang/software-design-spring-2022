package me.fang.kosh.commands

class Echo(private val args: List<String>) {
    // TODO: Process keys
    fun run(stdin: String = ""): String = args.fold("") { acc, s -> "$acc $s" }
}
