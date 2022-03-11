package me.fang.kosh.parser

import me.fang.kosh.Environment
import me.fang.kosh.exceptions.UnexpectedTokenException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun testParser() {
        assertEquals(Result.success(listOf(listOf("echo", "hello"))), parse("echo hello"))
        assertEquals(Result.success(listOf(listOf("echo", "hello"))), parse("e'ch'\"o\" hello"))
        assertEquals(Result.success(listOf(listOf("wc", "-l"))), parse("wc -l"))
        assertEquals(Result.success(listOf(listOf("echo", "\\"))), parse("echo \\\\\$x"))
        assertEquals(
            Result.success(listOf(listOf("wc", "-l", "--files0-from=file name.txt"))),
            parse("wc -l --files0-from='file name.txt'")
        )
        assertEquals(
            Result.success(listOf(listOf("cat", "/etc/hosts"), listOf("wc", "-l"))),
            parse("cat /etc/hosts | wc -l")
        )
        assertEquals(Result.success(listOf(listOf("echo", "\$x"))), parse("echo '\$x'\"\$x\""))
        Environment.vars["x"] = "=2"
        assertEquals(Result.success(listOf(listOf("echo", "\$x=2"))), parse("echo '\$x'\"\$x\""))
        assertThrows<UnexpectedTokenException> { parse("").getOrThrow() }
        assertThrows<UnexpectedTokenException> { parse("| echo hello").getOrThrow() }
    }
}
