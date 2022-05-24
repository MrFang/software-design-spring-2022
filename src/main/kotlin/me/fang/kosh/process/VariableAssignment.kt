package me.fang.kosh.process

import me.fang.kosh.Environment

class VariableAssignment(override val args: List<String>) : KoshProcess {
    override fun run(cli: Cli): Int {
        val assigment = args[0].split("=", limit = 2)
        Environment.vars[assigment[0]] = assigment[1]

        if (args.size > 1) return cli.processSingleCommand(args.drop(1))

        return 0
    }
}
