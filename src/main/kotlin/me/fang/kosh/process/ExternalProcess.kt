package me.fang.kosh.process

import me.fang.kosh.Environment
import java.util.concurrent.TimeUnit
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

class ExternalProcess(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        val file = kotlin.io.path.createTempFile()
        file.writeText(stdin)
        val proc = ProcessBuilder(args)
            .directory(Environment.cwd.toFile())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .redirectInput(file.toFile())
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        file.deleteIfExists()
        return proc.inputStream.bufferedReader().readText()
    }
}
