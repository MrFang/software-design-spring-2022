package me.fang.kosh.process

import me.fang.kosh.exceptions.ExitCalledException
import me.fang.kosh.process.commands.Cat
import me.fang.kosh.process.commands.Echo
import me.fang.kosh.process.commands.Pwd
import me.fang.kosh.process.commands.Wc
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PipedInputStream

/**
 * Запускает команду.
 *
 * exit выбрасывает исключение, присваивание переменной мутирует окружение
 * @param cmdWithArgs список из команды и её аргументов
 * @return Стандартный вывод команды
 * @see [processPipeline]
 */
fun processSingleCommand(cmdWithArgs: List<String>) = when (cmdWithArgs[0]) {
    "cat" -> Cat(cmdWithArgs).run(System.`in`, System.out, System.err)
    "echo" -> Echo(cmdWithArgs).run(System.`in`, System.out, System.err)
    "wc" -> Wc(cmdWithArgs).run(System.`in`, System.out, System.err)
    "pwd" -> Pwd(cmdWithArgs).run(System.`in`, System.out, System.err)
    "exit" -> throw ExitCalledException()
    else -> if (cmdWithArgs[0].contains('=')) {
        VariableAssignment(cmdWithArgs).run(System.`in`, System.out, System.err)
    } else {
        ExternalProcess(cmdWithArgs).run(System.`in`, System.out, System.err)
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
fun processPipeline(pipeline: List<List<String>>) = pipeline.fold(System.`in`) { stdin, cmdWithArgs ->
    when (cmdWithArgs[0]) {
        "cat" -> {
            val s = ByteArrayOutputStream()
            Cat(cmdWithArgs).run(stdin, s, System.err)
            ByteArrayInputStream(s.toByteArray())
        }
        "echo" -> {
            val s = ByteArrayOutputStream()
            Echo(cmdWithArgs).run(stdin, s, System.err)
            ByteArrayInputStream(s.toByteArray())
        }
        "wc" -> {
            val s = ByteArrayOutputStream()
            Wc(cmdWithArgs).run(stdin, s, System.err)
            ByteArrayInputStream(s.toByteArray())
        }
        "pwd" -> {
            val inS = PipedInputStream()
            val s = ByteArrayOutputStream()
            Pwd(cmdWithArgs).run(stdin, s, System.err)
            ByteArrayInputStream(s.toByteArray())
        }
        "exit" -> ByteArrayInputStream(ByteArray(0)) // Exit on pipeline do nothing
        else -> if (cmdWithArgs[0].contains('=')) {
            ByteArrayInputStream(ByteArray(0)) // In pipeline assignment do nothing
        } else {
            val s = ByteArrayOutputStream()
            ExternalProcess(cmdWithArgs).run(stdin, s, System.err)
            ByteArrayInputStream(s.toByteArray())
        }
    }
}.transferTo(System.out)
