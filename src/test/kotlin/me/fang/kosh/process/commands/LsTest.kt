package me.fang.kosh.process.commands

import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.collections.ArrayList
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LsTest {
    @Test
    fun lsTest() {
        val testDir = Files.createTempDirectory("testDir")
        val expectedResult = ArrayList<String>()
        expectedResult.add(testDir.fileName.toString())
        val files = listOf("file1", "file2", "file3", "file4")
        for (file in files) {
            expectedResult.add(Files.createTempFile(testDir, file, ".txt").fileName.toString())
        }

        val output = Ls(listOf("ls", testDir.toAbsolutePath().toString())).run("").split(" ").toMutableList()
        output[output.size - 1] = output.last().trim()

        assertTrue(expectedResult.size == output.size && expectedResult.containsAll(output) && output.containsAll(expectedResult))
    }

    @Test
    fun lsTestException() {
        assertFailsWith<Exception> { Ls(listOf("ls", "0", "1")).run("") }
    }
}