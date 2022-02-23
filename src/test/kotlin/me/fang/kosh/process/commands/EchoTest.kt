package me.fang.kosh.process.commands

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EchoTest {
    @Test
    fun testEcho() {
        assertEquals("123\n", Echo(listOf("echo", "123")).run())
        assertEquals("123", Echo(listOf("echo", "123", "-n")).run())
        assertEquals("12\\n3\n", Echo(listOf("echo", "12\\n3")).run())
        assertEquals("--help\n", Echo(listOf("echo", "--help")).run())
        assertEquals("12\n3\n", Echo(listOf("echo", "-e", "12\\n3")).run())
        assertEquals("12\\n3\n", Echo(listOf("echo", "-eE", "12\\n3")).run())
    }
}