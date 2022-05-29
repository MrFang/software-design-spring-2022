package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.process.KoshProcess
import java.nio.file.Path
import kotlin.io.path.isDirectory

class Cd(override val args: List<String>) : KoshProcess {
    override fun run(stdin: String): String {
        val arg = args.drop(1)
        if (arg.size > 1) {
            throw IllegalStateException("Illegal number of arguments")
        }
        if (arg.size == 1) {
            val path = Path.of(arg[0])
            if (!path.isDirectory()) {
                throw IllegalStateException("Not a directory")
            }
            if (path.isAbsolute) {
                Environment.cwd = path
            } else {
                Environment.cwd = Environment.cwd.resolve(path)
            }
        }
        return ""
    }
}