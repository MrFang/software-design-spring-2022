package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.getResource
import me.fang.kosh.process.Cli
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
    override fun run(cli: Cli): Int {
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
                                cli.stdout.write("Invalid option $arg".toByteArray())
                                return 1
                            }

                            char == 'L'
                        }
                    }
                }
            }
        }

        if (help) {
            val file = getResource("/messages/commands-help/pwd.txt")
            return if (file == null) {
                cli.stderr.write("Internal error".toByteArray())
                1
            } else {
                cli.stdout.write((file.readText()).toByteArray())
                0
            }
        } else if (logical) {
            cli.stdout.write((Environment.cwd.toString() + '\n').toByteArray())
            return 0
        } else {
            cli.stdout.write((Environment.cwd.toRealPath().toString() + '\n').toByteArray())
            return 0
        }
    }
}
