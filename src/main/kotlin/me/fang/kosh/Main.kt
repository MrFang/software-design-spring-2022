package me.fang.kosh

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.parser.parse
import me.fang.kosh.process.Cli
import me.fang.kosh.process.DefaultCli

fun main() {
    val cli: Cli = DefaultCli(DefaultCommandMapper(), System.`in`, System.out, System.err)

    while (true) {
        print("kosh:$ ")
        val input = readLine() ?: return

        if (input.isBlank()) continue

        val commands = parse(input)

        if (commands.isFailure) {
            commands.exceptionOrNull()?.message?.let { System.err.println(it) }
            continue
        }

        try {
            val stdout = if (commands.getOrNull()?.size == 1) {
                cli.processSingleCommand(commands.getOrThrow()[0])
            } else {
                cli.processPipeline(commands.getOrThrow())
            }
        } catch (_: ExitCalledException) {
            return
        } catch (e: Exception) {
            System.err.println(e)
        }
    }
}
