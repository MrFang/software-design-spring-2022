package me.fang.kosh.process

import me.fang.kosh.Environment

class VariableAssigment(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        val assigment = args[0].split("=", limit = 2)
        Environment.vars[assigment[0]] = assigment[1]

        if (args.size > 1) return processSingleCommand(args.drop(1))

        return ""
    }
}
