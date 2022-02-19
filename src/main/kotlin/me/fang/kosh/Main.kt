package me.fang.kosh

import me.fang.kosh.commands.Cat
import me.fang.kosh.commands.Echo
import me.fang.kosh.commands.Pwd
import me.fang.kosh.commands.Wc
import me.fang.kosh.parser.commands

fun main() {
    while (true) {
        val commands = commands.parse(readLine() ?: return)

        when (commands) {
            null -> return
            else -> {
                val cmds = commands.second.map { cmdWithArgs -> cmdWithArgs.map { arg -> arg.applyEnv() } }
                for (command in cmds) {
                    val cmd = command[0]
                    val args = command.slice(1 until command.size)

                    when (cmd) {
                        "cat" -> Cat(args).run()
                        "echo" -> Echo(args).run()
                        "wc" -> Wc(args).run()
                        "pwd" -> Pwd(args).run()
                        "exit" -> return
                    }
                }
            }
        }
    }
}
