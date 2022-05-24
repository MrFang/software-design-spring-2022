package me.fang.kosh.process

import me.fang.kosh.exceptions.ExitCalledException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.nio.charset.Charset
import kotlin.test.assertEquals

class TestCli : AbstractProcessTest() {
    @Test
    fun testSingleExit() {
        assertThrows<ExitCalledException> { cli.processSingleCommand(listOf("exit")) }
    }

    @Test
    fun testSingleCommand() {
        assertEquals(0, cli.processSingleCommand(listOf("echo", "-n", "123", "456")))
        assertEquals(0, err.size())
        assertEquals("123 456", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testExitInPipeline() {
        assertDoesNotThrow { cli.processPipeline(listOf(listOf("echo"), listOf("exit"))) }
    }

    @Test
    fun testStdoutToStdin() {
        assertEquals(0, cli.processPipeline(listOf(listOf("echo", "123"), listOf("cat"))))
        assertEquals(0, err.size())
        assertEquals("123\n", out.toString(Charset.defaultCharset()))
    }
}
