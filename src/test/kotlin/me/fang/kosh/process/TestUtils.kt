package me.fang.kosh.process

import me.fang.kosh.exceptions.ExitCalledException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TestUtils {
    @Test
    fun testSingleCommand() {
        assertThrows<ExitCalledException> { processSingleCommand(listOf("exit")) }
        // TODO: Fix later with CLI abstraction
        // assertEquals("123 456", processSingleCommand(listOf("echo", "-n", "123", "456")))
        // assertEquals("${Environment.cwd}\n", processSingleCommand(listOf("pwd")))
        // assertEquals(
        //    getResource("/messages/commands-help/cat.txt")?.readText(),
        //    processSingleCommand(listOf("cat", "--help"))
        // )
    }

    @Test
    fun testPipeline() {
        assertDoesNotThrow { processPipeline(listOf(listOf("echo"), listOf("exit"))) }
        // TODO: Fix later with CLI abstraction
        // assertEquals("123\n", processPipeline(listOf(listOf("echo", "123"), listOf("cat"))))
        // assertEquals("2 -\n", processPipeline(listOf(listOf("echo", "123", "456"), listOf("wc", "-", "-w"))))
        // assertEquals(
        //    "123\$",
        //    processPipeline(
        //        listOf(
        //            listOf("echo", "-n", "123"),
        //            listOf("cat", "-E"),
        //            listOf("cat", "-")
        //        )
        //    )
        // )
    }
}
