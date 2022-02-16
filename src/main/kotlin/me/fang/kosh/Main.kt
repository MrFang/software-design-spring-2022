package me.fang.kosh

import java.io.File

val ALLOWED_COMMANDS: List<String> = listOf(
    "cat",
    "echo",
    "wc",
    "pwd",
    "exit",
)

fun main() {
    val env: Map<String, String> = HashMap()

    while (true) {
        val commands = (readLine() ?: return)
            .split('|')

        for (command in commands) {
            val cmdWithArgs = applyEnv(env, command)
                .trim()
                .split(' ')
            val cmd = cmdWithArgs[0]
            val args = cmdWithArgs.getOrElse(1) { "" }

            when (cmd) {
                "cat" -> args.split(' ').forEach { name ->
                    run {
                        File(name).forEachLine(Charsets.UTF_8) { println(it) }
                        println()
                    } 
                }
                "echo" -> println(args)
                "wc" -> args.split(' ').forEach() {
                    println(File(it).readText(Charsets.UTF_8).split(' ').size)
                }
                "pwd" -> println(System.getProperty("user.dir"))
                "exit" -> return
            }
        }
    }
}

fun applyEnv(env: Map<String, String>, command: String): String {
    var result = command
    "\$[a-zA-z_][a-zA-Z0-9_]*".toRegex()
        .findAll(command)
        .forEach {
            run {
                val varName = it.value.slice(1 until it.value.length)
                result = result.replace(varName, env[varName] ?: "")
            }
        }

    return result
}
