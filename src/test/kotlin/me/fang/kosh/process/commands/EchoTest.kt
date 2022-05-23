package me.fang.kosh.process.commands

import me.fang.kosh.process.AbstractProcessTest
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals

class EchoTest : AbstractProcessTest() {
    @Test
    fun testSimple() {
        assertEquals(0, Echo(listOf("echo", "123")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("123\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testWithoutNewline() {
        assertEquals(0, Echo(listOf("echo", "123", "-n")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("123", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testNoBackslashInterpretation() {
        assertEquals(0, Echo(listOf("echo", "12\\n3")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("12\\n3\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testNoHelp() {
        assertEquals(0, Echo(listOf("echo", "--help")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("--help\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testBackslashInterpretation() {
        assertEquals(0, Echo(listOf("echo", "-e", "12\\n3")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("12\n3\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testMultipleOptions() {
        assertEquals(0, Echo(listOf("echo", "-eE", "12\\n3")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("12\\n3\n", out.toString(Charset.defaultCharset()))
    }
}
