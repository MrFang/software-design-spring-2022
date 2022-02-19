package me.fang.kosh.commands

import java.io.File

class Cat(private val args: List<String>) {
    fun run(stdin: String = ""): String {
        return args
            .map { name -> File(name).readLines().fold("") { text, s -> "$text\n$s" } }
            .fold("") { out, text -> "$out\n\n$text" }
    }
}
