package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.getResource
import me.fang.kosh.process.AbstractProcessTest
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import kotlin.test.assertEquals

class PwdTest : AbstractProcessTest() {
    @Test
    fun testSimple() {
        assertEquals(0, Pwd(listOf("pwd")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("${Environment.cwd}\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testAbsolutePath() {
        assertEquals(0, Pwd(listOf("pwd", "-P")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("${Environment.cwd.toRealPath()}\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testHelp() {
        assertEquals(
            0,
            Pwd(listOf("pwd", "--help")).run(emptyIn, out, err)
        )
        assertEquals(0, err.size())
        assertEquals(getResource("/messages/commands-help/pwd.txt")?.readText(), out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testHelpOverridesOtherFlags() {
        assertEquals(
            0,
            Pwd(listOf("pwd", "--help", "-L")).run(emptyIn, out, err)
        )
        assertEquals(0, err.size())
        assertEquals(getResource("/messages/commands-help/pwd.txt")?.readText(), out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testMultipleOptions() {
        assertEquals(0, Pwd(listOf("pwd", "-PL")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("${Environment.cwd}\n", out.toString(Charset.defaultCharset()))
    }

    @Test
    fun testLastOptionHasThePower() {
        assertEquals(0, Pwd(listOf("pwd", "-PL", "-LP")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals("${Environment.cwd.toRealPath()}\n", out.toString(Charset.defaultCharset()))
    }
}
