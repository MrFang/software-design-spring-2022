package me.fang.kosh.process.commands

import me.fang.kosh.getResource
import me.fang.kosh.process.AbstractProcessTest
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals

class WcTest : AbstractProcessTest() {
    @Test
    fun testSimple() {
        assertEquals(0, Wc(listOf("wc", "-")).run(createDefaultCliWithStdin("1 2".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("1 2 3 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testOnlyWordCont() {
        assertEquals(0, Wc(listOf("wc", "-w", "-")).run(createDefaultCliWithStdin("Hello, World!".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("2 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testCharsAndBytesCount() {
        assertEquals(0, Wc(listOf("wc", "-mc", "-")).run(createDefaultCliWithStdin("Привет".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("6 12 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testLineCounts() {
        assertEquals(0, Wc(listOf("wc", "-l", "-")).run(createDefaultCliWithStdin("Two\nLines".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("2 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testMaxLineLength() {
        assertEquals(0, Wc(listOf("wc", "-L", "-")).run(createDefaultCliWithStdin("Короткая\nИ длинная строки".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("16 -\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testWithoutArgsReadsStdin() {
        assertEquals(0, Wc(listOf("wc")).run(createDefaultCliWithStdin("123".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("1 1 3\n", out.toString())
    }

    @Test
    fun testHelp() {
        assertEquals(
            0,
            Wc(listOf("wc", "--help")).run(cli)
        )
        assertEquals(0, err.size())
        assertEquals(getResource("/messages/commands-help/wc.txt")?.readText(), out.toString(Charset.defaultCharset()))
    }
}
