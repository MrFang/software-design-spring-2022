package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.getResource
import me.fang.kosh.process.KoshProcess

/**
 * Команда pwd
 */
class Pwd(override val args: List<String>) : KoshProcess {
    private var logical = true
    private var help = false

    /**
     * Возвращает текущую рабочую директорию
     */
    override fun run(stdin: String): String {
        args.drop(1).forEach { arg ->
            run {
                if (arg == "--help") {
                    help = true
                    return@forEach
                }

                if (arg.startsWith('-')) {
                    logical = arg.drop(1).fold(logical) { _, char ->
                        run {
                            if (!"PL".contains(char)) {
                                return "Invalid option $arg"
                            }

                            char == 'L'
                        }
                    }
                }
            }
        }

        return if (help) {
            val file = getResource("/messages/commands-help/pwd.txt")
            if (file == null) {
                System.err.println("Internal error")
                ""
            } else {
                file.readText()
            }
        } else if (logical) {
            Environment.cwd.toString()
        } else {
            Environment.cwd.toRealPath().toString()
        } + '\n'
    }
}
