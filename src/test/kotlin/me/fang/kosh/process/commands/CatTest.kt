package me.fang.kosh.process.commands

import me.fang.kosh.getResource
import me.fang.kosh.process.AbstractProcessTest
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CatTest : AbstractProcessTest() {
    @Test
    fun testSimple() {
        assertEquals(1, Cat(listOf("cat")).run(createDefaultCliWithStdin("123".byteInputStream())))
        assertTrue(err.size() > 0)
    }

    @Test
    fun testLineNumbers() {
        assertEquals(0, Cat(listOf("cat", "-", "-n")).run(createDefaultCliWithStdin("123".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("1 123", out.toByteArray().toString(Charset.defaultCharset()))
    }

    @Test
    fun testPrintLineEndings() {
        assertEquals(0, Cat(listOf("cat", "-", "-E")).run(createDefaultCliWithStdin("123".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("123\$", out.toByteArray().toString(Charset.defaultCharset()))
    }

    @Test
    fun testCollapseLineEndings() {
        assertEquals(0, Cat(listOf("cat", "-", "-s")).run(createDefaultCliWithStdin("123\n\n\n456".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("123\n\n456", out.toByteArray().toString(Charset.defaultCharset()))
    }

    @Test
    fun testNonEmptyLineNumbers() {
        assertEquals(0, Cat(listOf("cat", "-", "-b")).run(createDefaultCliWithStdin("123\n\n\n456".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("1 123\n\n\n2 456", out.toByteArray().toString(Charset.defaultCharset()))
    }

    @Test
    fun testMultipleFlags() {
        assertEquals(0, Cat(listOf("cat", "-", "-ns")).run(createDefaultCliWithStdin("123\n\n\n456".byteInputStream())))
        assertEquals(0, err.size())
        assertEquals("1 123\n2\n3 456", out.toByteArray().toString(Charset.defaultCharset()))
    }

    @Test
    fun testHelp() {
        assertEquals(0, Cat(listOf("cat", "--help")).run(cli))
        assertEquals(0, err.size())
        assertEquals(getResource("/messages/commands-help/cat.txt")?.readText(), out.toByteArray().toString(Charset.defaultCharset()))
    }
}
