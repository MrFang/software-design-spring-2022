package me.fang.kosh

import me.fang.kosh.process.KoshProcess
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

abstract class Cli (
    private val mapper: CommandMapper,
    private val stdin: InputStream,
    private val stdout: OutputStream,
    private val stderr: OutputStream,
) {
    /**
     * Запускает команду.
     *
     * exit выбрасывает исключение, присваивание переменной мутирует окружение
     * @param cmdWithArgs список из команды и её аргументов
     * @return Код выхода пайплайна
     * @see [processPipeline]
     */
    abstract fun processSingleCommand(cmdWithArgs: List<String>): Int

    /**
     * Запускает пайплайн.
     *
     * exit и присваивание переменной не имеют эффекта.
     * @param pipeline список команд вместе с их аргументами
     * @return Код выхода пайплайна
     * @see [processSingleCommand]
     */
    abstract fun processPipeline(pipeline: List<List<String>>): Int
}

class DefaultCli(
    private val mapper: CommandMapper,
    private val stdin: InputStream,
    private val stdout: OutputStream,
    private val stderr: OutputStream,
) : Cli(mapper, stdin, stdout, stderr) {

    override fun processSingleCommand(cmdWithArgs: List<String>) = getCmd(cmdWithArgs).run(stdin, stdout, stderr)

    private fun getCmd(cmdWithArgs: List<String>): KoshProcess {
        val cmdCtor = mapper.commandMapping[cmdWithArgs[0]]
        return if (cmdCtor != null) {
            cmdCtor(cmdWithArgs)
        } else if (cmdWithArgs[0].contains('=')) {
            mapper.variableAssigmentConstructor(cmdWithArgs)
        } else {
            mapper.externalProcessConstructor(cmdWithArgs)
        }
    }

    override fun processPipeline(pipeline: List<List<String>>): Int {
        pipeline.fold(stdin) {stdin, cmdWithArgs -> run {
          val cmd = getCmd(cmdWithArgs)
          val s = ByteArrayOutputStream()
          cmd.run(stdin, s, stderr)
          ByteArrayInputStream(s.toByteArray())
        } }.transferTo(stdout)

        return 0
    }
}