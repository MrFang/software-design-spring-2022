package me.fang.kosh.process.commands

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GrepTest {
    @Test
    fun testGrep() {
        assertEquals("123\n", Grep(listOf("grep", "123", "-")).run("123"))
        assertEquals("word\nlongword\n", Grep(listOf("grep", "word")).run("word\nlongword"))
        assertEquals("word\n", Grep(listOf("grep", "-w", "word")).run("word\nlongword"))
        assertEquals(
            "word\nlongword\n",
            Grep(listOf("grep", "-w", "-A", "2", "word")).run("word\nlongword")
        )
        assertEquals("WORD\n", Grep(listOf("grep", "-i", "word")).run("WORD"))
    }
}
