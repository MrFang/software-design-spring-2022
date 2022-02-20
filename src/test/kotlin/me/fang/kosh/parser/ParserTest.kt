package me.fang.kosh.parser

import me.fang.kosh.Environment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun testCommands() {
        assertEquals(Pair("", listOf(listOf("echo", "hello"))), commands.parse("echo hello"))
        assertEquals(Pair("", listOf(listOf("echo", "hello"))), commands.parse("e'ch'\"o\" hello"))
        assertEquals(Pair("", listOf(listOf("wc", "-l"))), commands.parse("wc -l"))
        assertEquals(Pair("", listOf(listOf("wc", "-l", "--files0-from=file name.txt"))), commands.parse("wc -l --files0-from='file name.txt'"))
        assertEquals(Pair("", listOf(listOf("cat", "/etc/hosts"), listOf("wc", "-l"))), commands.parse("cat /etc/hosts | wc -l"))
        assertEquals(Pair("", listOf(listOf("echo", "\$x"))), commands.parse("echo '\$x'\"\$x\""))
        Environment.vars["x"] = "=2"
        assertEquals(Pair("", listOf(listOf("echo", "\$x=2"))), commands.parse("echo '\$x'\"\$x\""))
    }
}
