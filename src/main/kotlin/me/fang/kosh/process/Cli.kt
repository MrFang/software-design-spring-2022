package me.fang.kosh.process

import me.fang.kosh.CommandMapper
import me.fang.kosh.exceptions.ExitCalledException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

interface Cli {
    val mapper: CommandMapper
    val stdin: InputStream
    val stdout: OutputStream
    val stderr: OutputStream

    /**
     * Запускает команду.
     *
     * exit выбрасывает исключение, присваивание переменной мутирует окружение
     * @param cmdWithArgs список из команды и её аргументов
     * @return Код выхода пайплайна
     * @see [processPipeline]
     */
    fun processSingleCommand(cmdWithArgs: List<String>): Int

    /**
     * Запускает пайплайн.
     *
     * exit и присваивание переменной не имеют эффекта.
     * @param pipeline список команд вместе с их аргументами
     * @return Код выхода пайплайна
     * @see [processSingleCommand]
     */
    fun processPipeline(pipeline: List<List<String>>): Int
}

class DefaultCli(
    override val mapper: CommandMapper,
    override val stdin: InputStream,
    override val stdout: OutputStream,
    override val stderr: OutputStream,
) : Cli {

    override fun processSingleCommand(cmdWithArgs: List<String>) = getCmd(cmdWithArgs).run(this)

    private fun getCmd(cmdWithArgs: List<String>): KoshProcess {
        val cmdCtor = mapper.commandMapping[cmdWithArgs[0]]
        val cmd = (
            if (cmdCtor != null) {
                cmdCtor(cmdWithArgs)
            } else if (cmdWithArgs[0].contains('=')) {
                mapper.variableAssigmentConstructor(cmdWithArgs)
            } else {
                mapper.externalProcessConstructor(cmdWithArgs)
            }
            )

        return cmd
    }

    override fun processPipeline(pipeline: List<List<String>>): Int {
        pipeline.fold(stdin) { stdin, cmdWithArgs ->
            run {
                val cmd = getCmd(cmdWithArgs)
                val s = ByteArrayOutputStream()
                try {
                    cmd.run(DefaultCli(mapper, stdin, s, stderr))
                } catch (ignore: ExitCalledException) {}
                ByteArrayInputStream(s.toByteArray())
            }
        }.transferTo(stdout)

        return 0
    }
}
