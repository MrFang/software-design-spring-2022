package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CdTest {
    @Test
    fun cdTest() {
        assertEquals("", Cd(listOf("cd")).run(""))
        Cd(listOf("cd", "..")).run("")
        assertEquals(Path.of("..").toAbsolutePath().toString(), Environment.cwd.toString())
        assertFailsWith<Exception>{ Cd(listOf("cd", "..", "/")).run("") }
        val file = Files.createTempFile("tmp_file", ".txt")
        assertFailsWith<Exception> { Cd(listOf("cd", file.toAbsolutePath().toString())).run("") }
        val dir = Files.createTempDirectory("tmp_dir")
        Cd(listOf("cd", dir.toAbsolutePath().toString())).run("")
        assertEquals(dir.toAbsolutePath().toString(), Environment.cwd.toString())
    }
}