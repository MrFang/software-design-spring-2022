package me.fang.kosh.process

import me.fang.kosh.Environment
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readBytes
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
    override fun run(stdin: InputStream, stdout: OutputStream, stderr: OutputStream): Int {
        val `in` = kotlin.io.path.createTempFile()
        val out = kotlin.io.path.createTempFile()
        val err = kotlin.io.path.createTempFile()
        `in`.writeText(stdin.readBytes().toString(Charset.defaultCharset()))
        val proc = ProcessBuilder(args)
            .directory(Environment.cwd.toFile())
            .redirectInput(`in`.toFile())
            .redirectOutput(out.toFile())
            .redirectError(err.toFile())
            .start()

        return if (proc.waitFor(60, TimeUnit.MINUTES)) {
            stdout.write(out.readBytes())
            stderr.write(err.readBytes())
            `in`.deleteIfExists()
            out.deleteIfExists()
            err.deleteIfExists()
            0
        } else {
            `in`.deleteIfExists()
            out.deleteIfExists()
            err.deleteIfExists()
            1
        }
    }
}
