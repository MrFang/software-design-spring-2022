package me.fang.kosh.process.commands

import me.fang.kosh.getResource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CatTest {
    @Test
    fun testCat() {
        assertEquals("123", Cat(listOf("cat")).run("123"))
        assertEquals("1 123", Cat(listOf("cat", "-n")).run("123"))
        assertEquals("123$", Cat(listOf("cat", "-E")).run("123"))
        assertEquals("123\n\n456", Cat(listOf("cat", "-s")).run("123\n\n\n456"))
        assertEquals("1 123\n\n\n2 456", Cat(listOf("cat", "-b")).run("123\n\n\n456"))
        assertEquals("1 123\n2\n3 456", Cat(listOf("cat", "-ns")).run("123\n\n\n456"))
        assertEquals(getResource("/messages/commands-help/cat.txt")?.readText(), Cat(listOf("cat", "--help")).run())
    }
}
