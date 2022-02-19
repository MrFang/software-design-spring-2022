package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.process.KoshProcess

class Pwd(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        return Environment.cwd.toString()
    }
}
