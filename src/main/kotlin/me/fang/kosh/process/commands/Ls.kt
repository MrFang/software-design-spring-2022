package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.process.KoshProcess
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class Ls(override val args: List<String>) : KoshProcess{
    override fun run(stdin: String): String {
        val filename = args.drop(1)
        return if (filename.isEmpty()) {
            getFiles(Environment.cwd)
        } else if (filename.size == 1) {
            getFiles(Paths.get(filename[0]))
        } else {
            throw IllegalStateException("Illegal number of arguments")
        }
    }

    private fun getFiles(path: Path): String {
        val newPath = if (!path.isAbsolute) {
            Environment.cwd.resolve(path)
        } else {
            path
        }

        return Files.walk(newPath, 1).map{ x -> x.fileName.toString()}.collect(Collectors.joining(" ")) + "\n"
    }
}