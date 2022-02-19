package me.fang.kosh

import me.fang.kosh.commands.Cat
import me.fang.kosh.commands.Echo
import me.fang.kosh.commands.Pwd
import me.fang.kosh.commands.Wc
import me.fang.kosh.parser.commands
import me.fang.kosh.parser.token.SingleQuotedString

fun main() {
    while (true) {
        val commands = commands.parse(readLine() ?: return)

        when (commands) {
            null -> return
            else -> {
                commands.second.fold("") { stdin, cmdWithArgs ->
                    run {
                        val command = cmdWithArgs.map { token ->
                            when (token) {
                                is SingleQuotedString -> token.s
                                else -> token.s.applyEnv()
                            }
                        }
                        val cmd = command[0]
                        val args = command.drop(1)

                        when (cmd) {
                            "cat" -> Cat(args).run(stdin)
                            "echo" -> Echo(args).run(stdin)
                            "wc" -> Wc(args).run(stdin)
                            "pwd" -> Pwd(args).run(stdin)
                            "exit" -> ""
                            else -> "" // TODO: External process
                        }
                    }
                }
            }
        }
    }
}
