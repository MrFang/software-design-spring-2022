package me.fang.kosh.process

interface KoshProcess {
    val args: List<String>

    fun run(stdin: String = ""): String
}
