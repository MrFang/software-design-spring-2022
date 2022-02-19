package me.fang.kosh.commands

import me.fang.kosh.Environment

class Pwd(private val args: List<String>) {
    fun run(stdin: String = ""): String {
        return Environment.cwd.toString()
    }
}
