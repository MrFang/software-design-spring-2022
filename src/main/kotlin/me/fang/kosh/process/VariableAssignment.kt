package me.fang.kosh.process

import me.fang.kosh.Environment
import java.io.InputStream
import java.io.OutputStream

class VariableAssignment(override val args: List<String>) : KoshProcess {
    override fun run(stdin: InputStream, stdout: OutputStream, stderr: OutputStream): Int {
        val assigment = args[0].split("=", limit = 2)
        Environment.vars[assigment[0]] = assigment[1]

        if (args.size > 1) return processSingleCommand(args.drop(1))

        return 0
    }
}
