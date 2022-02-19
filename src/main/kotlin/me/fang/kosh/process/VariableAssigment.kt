package me.fang.kosh.process

import me.fang.kosh.Environment

class VariableAssigment(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        val assigment = args[0].split(' ', limit = 1)
        Environment.vars[assigment[0]] = assigment[1]
        return ""
    }
}
