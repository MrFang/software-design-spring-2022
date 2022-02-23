package me.fang.kosh.process

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.process.commands.Cat
import me.fang.kosh.process.commands.Echo
import me.fang.kosh.process.commands.Pwd
import me.fang.kosh.process.commands.Wc

fun processSingleCommand(cmdWithArgs: List<String>): String = when (cmdWithArgs[0]) {
    "cat" -> Cat(cmdWithArgs).run()
    "echo" -> Echo(cmdWithArgs).run()
    "wc" -> Wc(cmdWithArgs).run()
    "pwd" -> Pwd(cmdWithArgs).run()
    "exit" -> throw ExitCalledException()
    else -> if (cmdWithArgs[0].contains('=')) {
        VariableAssigment(cmdWithArgs).run()
    } else {
        ExternalProcess(cmdWithArgs).run()
    }
}

fun processPipeline(pipeline: List<List<String>>): String = pipeline.fold("") { stdin, cmdWithArgs ->
    when (cmdWithArgs[0]) {
        "cat" -> Cat(cmdWithArgs).run(stdin)
        "echo" -> Echo(cmdWithArgs).run(stdin)
        "wc" -> Wc(cmdWithArgs).run(stdin)
        "pwd" -> Pwd(cmdWithArgs).run(stdin)
        "exit" -> "" // Exit on pipeline do nothing
        else -> if (cmdWithArgs[0].contains('=')) {
            "" // In pipeline assigment do nothing
        } else {
            ExternalProcess(cmdWithArgs).run(stdin)
        }
    }
}
