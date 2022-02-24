package me.fang.kosh.process

import me.fang.kosh.Environment
import java.util.concurrent.TimeUnit
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

/**
 * Внешний процесс
 */
class ExternalProcess(override val args: List<String>) : KoshProcess {
    /**
     * Запускает внешний процесс, скармливает ему переданный stdin
     * ловит его stdout и возвращает.
     * Ждёт завершения процесса в течение часа, если не дожидается, возвращает пустой stdout
     */
    override fun run(stdin: String): String {
        val file = kotlin.io.path.createTempFile()
        file.writeText(stdin)
        val proc = ProcessBuilder(args)
            .directory(Environment.cwd.toFile())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .redirectInput(file.toFile())
            .start()

        return if (proc.waitFor(60, TimeUnit.MINUTES)) {
            file.deleteIfExists()
            proc.inputStream.bufferedReader().readText()
        } else {
            file.deleteIfExists()
            ""
        }
    }
}
