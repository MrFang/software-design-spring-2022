package me.fang.kosh

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.parser.commands
import me.fang.kosh.process.VariableAssigment
import me.fang.kosh.process.commands.Cat
import me.fang.kosh.process.commands.Echo
import me.fang.kosh.process.commands.Pwd
import me.fang.kosh.process.commands.Wc

fun main() {
    while (true) {
        val commands = commands.parse(readLine() ?: return)

        when (commands) {
            null -> return
            else -> {
                val stdout = if (commands.second.size == 1) {
                    try {
                        processSingleCommand(commands.second[0].map { tokenToString(it) })
                    } catch (_: ExitCalledException) {
                        return
                    }
                } else {
                    processPipeline(commands.second.map { l -> l.map { tokenToString(it) } })
                }

                println(stdout)
            }
        }
    }
}

fun processSingleCommand(cmdWithArgs: List<String>): String {
    val cmd = cmdWithArgs[0]
    val args = cmdWithArgs.drop(1)

    return when (cmd) {
        "cat" -> Cat(args).run()
        "echo" -> Echo(args).run()
        "wc" -> Wc(args).run()
        "pwd" -> Pwd(args).run()
        "exit" -> throw ExitCalledException()
        else -> if (cmd.contains('=')) {
            VariableAssigment(cmdWithArgs).run()
        } else {
            "" // TODO: External process
        }
    }
}

fun processPipeline(pipeline: List<List<String>>): String = pipeline.fold("") { stdin, cmdWithArgs ->
    run {
        val cmd = cmdWithArgs[0]
        val args = cmdWithArgs.drop(1)

        when (cmd) {
            "cat" -> Cat(args).run(stdin)
            "echo" -> Echo(args).run(stdin)
            "wc" -> Wc(args).run(stdin)
            "pwd" -> Pwd(args).run(stdin)
            "exit" -> "" // Exit on pipeline do nothing
            else -> if (cmd.contains('=')) {
                "" // In pipeline assigment do nothing
            } else {
                "" // TODO: External process
            }
        }
    }
}
