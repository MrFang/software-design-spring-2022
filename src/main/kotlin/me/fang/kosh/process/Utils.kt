package me.fang.kosh.process

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.process.commands.*

/**
 * Запускает команду.
 *
 * exit выбрасывает исключение, присваивание переменной мутирует окружение
 * @param cmdWithArgs список из команды и её аргументов
 * @return Стандартный вывод команды
 * @see [processPipeline]
 */
fun processSingleCommand(cmdWithArgs: List<String>): String = when (cmdWithArgs[0]) {
    "cat" -> Cat(cmdWithArgs).run("")
    "echo" -> Echo(cmdWithArgs).run("")
    "wc" -> Wc(cmdWithArgs).run("")
    "pwd" -> Pwd(cmdWithArgs).run("")
    "grep" -> Grep(cmdWithArgs).run("")
    "cd" -> Cd(cmdWithArgs).run("")
    "ls" -> Ls(cmdWithArgs).run("")
    "exit" -> throw ExitCalledException()
    else -> if (cmdWithArgs[0].contains('=')) {
        VariableAssignment(cmdWithArgs).run("")
    } else {
        ExternalProcess(cmdWithArgs).run("")
    }
}

/**
 * Запускает пайплайн.
 *
 * exit и присваивание переменной не имеют эффекта.
 * @param pipeline список команд вместе с их аргументами
 * @return stdout последней команды в пайплайне
 * @see [processSingleCommand]
 */
fun processPipeline(pipeline: List<List<String>>): String = pipeline.fold("") { stdin, cmdWithArgs ->
    when (cmdWithArgs[0]) {
        "cat" -> Cat(cmdWithArgs).run(stdin)
        "echo" -> Echo(cmdWithArgs).run(stdin)
        "wc" -> Wc(cmdWithArgs).run(stdin)
        "pwd" -> Pwd(cmdWithArgs).run(stdin)
        "grep" -> Grep(cmdWithArgs).run(stdin)
        "cd" -> Cd(cmdWithArgs).run(stdin)
        "ls" -> Ls(cmdWithArgs).run(stdin)
        "exit" -> "" // Exit on pipeline do nothing
        else -> if (cmdWithArgs[0].contains('=')) {
            "" // In pipeline assignment do nothing
        } else {
            ExternalProcess(cmdWithArgs).run(stdin)
        }
    }
}
