package me.fang.kosh.process.commands

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WcTest {
    @Test
    fun testWc() {
        assertEquals("1 2 3 -", Wc(listOf("wc", "-")).run("1 2"))
        assertEquals("2 -", Wc(listOf("wc", "-w", "-")).run("Hello, World!"))
        assertEquals("6 12 -", Wc(listOf("wc", "-mc", "-")).run("Привет"))
        assertEquals("2 -", Wc(listOf("wc", "-l", "-")).run("Two\nLines"))
        assertEquals("16 -", Wc(listOf("wc", "-L", "-")).run("Короткая\nИ длинная строки"))
        assertEquals(
            this::class.java.getResource("/messages/commands-help/wc.txt")?.readText(),
            Wc(listOf("wc", "--help")).run()
        )
    }
}
