package me.fang.kosh.process.commands

import me.fang.kosh.getResource
import me.fang.kosh.process.AbstractProcessTest
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals

class WcTest : AbstractProcessTest() {
    @Test
    fun testSimple() {
        assertEquals(0, Wc(listOf("wc", "-")).run("1 2".byteInputStream(), out, err))
        assertEquals(0, err.size())
        assertEquals("1 2 3 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testOnlyWordCont() {
        assertEquals(0, Wc(listOf("wc", "-w", "-")).run("Hello, World!".byteInputStream(), out, err))
        assertEquals(0, err.size())
        assertEquals("2 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testCharsAndBytesCount() {
        assertEquals(0, Wc(listOf("wc", "-mc", "-")).run("Привет".byteInputStream(), out, err))
        assertEquals(0, err.size())
        assertEquals("6 12 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testLineCounts() {
        assertEquals(0, Wc(listOf("wc", "-l", "-")).run("Two\nLines".byteInputStream(), out, err))
        assertEquals(0, err.size())
        assertEquals("2 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testMaxLineLength() {
        assertEquals(0, Wc(listOf("wc", "-L", "-")).run("Короткая\nИ длинная строки".byteInputStream(), out, err))
        assertEquals(0, err.size())
        assertEquals("16 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testWithoutArgsReadsStdin() {
        assertEquals(0, Wc(listOf("wc")).run("123".byteInputStream(), out, err))
        assertEquals(0, err.size())
        assertEquals("1 1 3\n", out.toString())
    }

    @Test
    fun testHelp() {
        assertEquals(
            0,
            Wc(listOf("wc", "--help")).run(emptyIn, out, err)
        )
        assertEquals(0, err.size())
        assertEquals(getResource("/messages/commands-help/wc.txt")?.readText(), out.toString(Charset.defaultCharset()))
    }
}
