package me.fang.kosh.commands

import java.io.File

class Wc(private val args: List<String>) {
    fun run(stdin: String = ""): String {
        return args
            .map { File(it).readText(Charsets.UTF_8).split(' ').size }
            .fold("") { acc, count -> "$acc\n$count" }
    }
}
