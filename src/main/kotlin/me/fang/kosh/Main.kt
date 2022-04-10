package me.fang.kosh

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.parser.parse
import me.fang.kosh.process.processPipeline
import me.fang.kosh.process.processSingleCommand

fun main() {
    while (true) {
        print("kosh:$ ")
        val input = readLine() ?: return

        if (input.isBlank()) continue

        val commands = parse(input)

        if (commands.isFailure) {
            System.err.println(commands.exceptionOrNull()?.message)
            continue
        }

        try {
            val stdout = if (commands.getOrNull()?.size == 1) {
                processSingleCommand(commands.getOrThrow()[0])
            } else {
                processPipeline(commands.getOrThrow())
            }

            print(stdout)
        } catch (_: ExitCalledException) {
            return
        } catch (e: Exception) {
            System.err.println(e)
        }
    }
}
