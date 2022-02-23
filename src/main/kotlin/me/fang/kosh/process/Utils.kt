package me.fang.kosh.process

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.process.commands.Cat
import me.fang.kosh.process.commands.Echo
import me.fang.kosh.process.commands.Pwd
import me.fang.kosh.process.commands.Wc

/**
 * Запускает команду.
 *
 * exit выбрасывает исключеие, пристваивание переменной мутирует окружение
 * @param cmdWithArgs список из команды и её аргументов
 * @return стандартынй вывод команды
 * @see [processPipeline]
 */
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

/**
 * Запускает пайплайн.
 *
 * exit и присваивание переменной не имеют эффекта.
 * @param pipeline спискок команд вместе с их аргументами
 * @return stdout последней команды в пайплайне
 * @see [processSingleCommand]
 */
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
