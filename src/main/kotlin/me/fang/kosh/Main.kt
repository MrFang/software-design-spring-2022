package me.fang.kosh

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.parser.commands
import me.fang.kosh.process.processPipeline
import me.fang.kosh.process.processSingleCommand

fun main() {
    while (true) {
        print("kosh:$ ")
        val input = readLine() ?: return

        if (input.isBlank()) continue

        val commands = commands.parse(input)

        if (commands == null) {
            System.err.println("Syntax error on '${input[0]}'")
            continue
        }

        if (commands.first != "") {
            System.err.println("Syntax error on '${commands.first[0]}'")
            continue
        }

        try {
            val stdout = if (commands.second.size == 1) {
                processSingleCommand(commands.second[0])
            } else {
                processPipeline(commands.second)
            }

            print(stdout)
        } catch (_: ExitCalledException) {
            return
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }
}
